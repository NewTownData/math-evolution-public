/*
 * Copyright 2023 Voyta Krizek, https://github.com/NewTownData
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.newtowndata.math.serialization;

import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.genetics.nodes.GroupConstantNode;
import com.newtowndata.math.genetics.nodes.IntConstantNode;
import com.newtowndata.math.genetics.nodes.SpecialConstantNode;
import com.newtowndata.math.genetics.nodes.VariableNode;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.helper.Constant;
import com.newtowndata.math.model.Model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModelSerializer {

  private static final Pattern LINE_PATTERN =
      Pattern.compile("^(y[0-9]{3})=([A-Z][A-Za-z0-9]+)\\(([^)]+)\\)$");

  private static final String CLASS_NAME_PREFIX = Node.class.getName().replace("core.Node", "");

  private static final Pattern NAMED_CONSTANT_PATTERN = Pattern.compile("^([^]]+)\\[([^]]+)\\]$");

  public String serialize(Model model) {
    final AtomicInteger nodeIndex = new AtomicInteger();

    LinkedList<IndexedNode> openList = new LinkedList<>();
    openList.add(new IndexedNode(nodeIndex.getAndIncrement(), model.getRoot()));

    List<String> output = new ArrayList<>();

    while (!openList.isEmpty()) {
      IndexedNode currentNode = openList.removeFirst();

      List<IndexedNode> children = currentNode.getNode().getChildren().stream()
          .map(n -> new IndexedNode(nodeIndex.getAndIncrement(), n)).collect(Collectors.toList());

      children.forEach(openList::addLast);

      output.add(serializeNode(currentNode, children));
    }

    StringBuilder sb = new StringBuilder();
    Collections.reverse(output);
    output.forEach(row -> {
      sb.append(row);
      sb.append('\n');
    });

    return sb.toString();
  }

  public Model deserialize(String input) {
    Map<String, Node> parsedNodes = new HashMap<>();

    Node root = null;

    StringTokenizer lineTokenizer = new StringTokenizer(input, "\n");
    while (lineTokenizer.hasMoreTokens()) {
      String line = lineTokenizer.nextToken().trim();
      if (line.isEmpty()) {
        continue;
      }

      root = deserializeNode(parsedNodes, line);
    }

    if (root == null) {
      throw new IllegalArgumentException("No model parsed");
    }

    return new Model(root);
  }

  Node deserializeNode(Map<String, Node> nodes, String line) {
    Matcher lineMatcher = LINE_PATTERN.matcher(line);
    if (!lineMatcher.find()) {
      throw new IllegalArgumentException("Cannot parse line: " + line);
    }

    String nodeIndex = lineMatcher.group(1);
    String nodeSimpleName = lineMatcher.group(2);
    String nodeParameters = lineMatcher.group(3);

    try {
      Node currentNode;
      Class<?> nodeClass = Class.forName(CLASS_NAME_PREFIX + nodeSimpleName);
      if (LeafNode.class.isAssignableFrom(nodeClass)) {
        currentNode = deserializeLeafNode(nodeClass, nodeParameters);
      } else {
        currentNode = deserializeNonLeafNode(nodes, nodeClass, nodeParameters);
      }
      nodes.put(nodeIndex, currentNode);
      return currentNode;
    } catch (Exception e) {
      throw new IllegalArgumentException("Node " + nodeSimpleName + " on index " + nodeIndex
          + " with parameter " + nodeParameters + " cannot be created", e);
    }
  }

  Node deserializeNonLeafNode(Map<String, Node> nodes, Class<?> nodeClass, String nodeParameters) {
    Node currentNode;
    String[] parameterNodeIndices = nodeParameters.split(",");
    if (parameterNodeIndices.length == 0) {
      throw new IllegalArgumentException("No parameter provided");
    }

    Class<?>[] constructorNodes = new Class[parameterNodeIndices.length];
    Object[] childNodes = new Object[parameterNodeIndices.length];
    for (int i = 0; i < childNodes.length; i++) {
      Node childNode = nodes.get(parameterNodeIndices[i]);
      if (childNode == null) {
        throw new IllegalArgumentException(
            "Cannot find child node of index " + parameterNodeIndices[i]);
      }

      childNodes[i] = childNode;
      constructorNodes[i] = Node.class;
    }

    try {
      currentNode = (Node) nodeClass.getConstructor(constructorNodes).newInstance(childNodes);
      return currentNode;
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot create node " + nodeClass.getName(), e);
    }
  }

  Node deserializeLeafNode(Class<?> clazz, String parameters) {
    if (ConstantNode.class.equals(clazz)) {
      return new ConstantNode(Double.parseDouble(parameters));
    } else if (IntConstantNode.class.equals(clazz)) {
      return new IntConstantNode(Integer.parseInt(parameters));
    } else if (GroupConstantNode.class.equals(clazz)) {
      StringTokenizer parameterTokenizer = new StringTokenizer(parameters, ",");
      List<Constant> constants = new ArrayList<>();
      while (parameterTokenizer.hasMoreTokens()) {
        Matcher m = NAMED_CONSTANT_PATTERN.matcher(parameterTokenizer.nextToken());
        if (!m.find()) {
          throw new IllegalArgumentException(
              "Parameter " + parameters + " is not a group constant");
        }
        constants.add(new Constant(m.group(1), Double.parseDouble(m.group(2))));
      }
      return new GroupConstantNode(constants);
    } else if (SpecialConstantNode.class.equals(clazz)) {
      Matcher m = NAMED_CONSTANT_PATTERN.matcher(parameters);
      if (!m.find()) {
        throw new IllegalArgumentException("Parameter " + parameters + " is not special constant");
      }
      return SpecialConstantNode.of(m.group(1));
    } else if (VariableNode.class.equals(clazz)) {
      return new VariableNode(Integer.parseInt(parameters));
    }
    throw new IllegalArgumentException("Unsupported node " + clazz.getSimpleName());
  }

  String serializeNode(IndexedNode node, List<IndexedNode> children) {
    Node currentNode = node.getNode();

    StringBuilder sb = new StringBuilder();
    sb.append(serializeIndex(node.getNodeIndex()));
    sb.append("=");
    sb.append(currentNode.getClass().getSimpleName());
    sb.append('(');
    if (currentNode instanceof LeafNode) {
      sb.append(serializeLeafNode(currentNode));
    } else {
      if (children.isEmpty()) {
        throw new IllegalArgumentException("Non-leaf node must have child nodes");
      }

      for (int i = 0; i < children.size(); i++) {
        if (i > 0) {
          sb.append(",");
        }
        sb.append(serializeIndex(children.get(i).getNodeIndex()));
      }
    }
    sb.append(')');
    return sb.toString();
  }

  String serializeLeafNode(Node node) {
    if (node instanceof ConstantNode) {
      return Double.toString(((ConstantNode) node).getConstant());
    } else if (node instanceof IntConstantNode) {
      return Integer.toString(((IntConstantNode) node).getConstant());
    } else if (node instanceof GroupConstantNode) {
      StringBuilder sb = new StringBuilder();
      for (Constant constant : ((GroupConstantNode) node).getConstants()) {
        if (sb.length() > 0) {
          sb.append(',');
        }
        sb.append(constantToString(constant));
      }
      return sb.toString();
    } else if (node instanceof SpecialConstantNode) {
      Constant constant = ((SpecialConstantNode) node).getConstant();
      return constantToString(constant);
    } else if (node instanceof VariableNode) {
      return Integer.toString(((VariableNode) node).getXIndex());
    }
    throw new IllegalArgumentException("Unsupported node " + node.getClass().getSimpleName());
  }

  private String constantToString(Constant constant) {
    return constant.toString() + "[" + constant.getValue() + "]";
  }

  String serializeIndex(int index) {
    return "y" + String.format(Locale.ENGLISH, "%03d", index);
  }

  static class IndexedNode {

    private final int nodeIndex;
    private final Node node;

    public IndexedNode(int nodeIndex, Node node) {
      this.nodeIndex = nodeIndex;
      this.node = node;
    }

    public Node getNode() {
      return node;
    }

    public int getNodeIndex() {
      return nodeIndex;
    }
  }

}
