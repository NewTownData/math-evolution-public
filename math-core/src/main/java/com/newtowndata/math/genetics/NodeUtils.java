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
package com.newtowndata.math.genetics;

import com.newtowndata.math.genetics.nodes.core.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public final class NodeUtils {

  private NodeUtils() {
  }

  public static int calculateSize(Node root) {
    LinkedList<Node> openList = new LinkedList<>();
    openList.add(root);

    int totalSize = 0;
    while (openList.size() > 0) {
      Node node = openList.removeFirst();
      totalSize++;
      node.getChildren().forEach(openList::addLast);
    }
    return totalSize;
  }

  public static Node extractNode(Node root, int nodeIndex) {
    LinkedList<Node> openList = new LinkedList<>();
    openList.add(root);

    int totalSize = 0;
    while (openList.size() > 0) {
      Node node = openList.removeFirst();
      if (totalSize == nodeIndex) {
        return node;
      }
      totalSize++;
      node.getChildren().forEach(openList::addLast);
    }
    return root;
  }

  public static Node mutateNode(Node root, int nodeIndex, MutationContext mutationContext) {
    return replaceNode(root, nodeIndex, node -> node.mutate(mutationContext));
  }

  public static Node replaceNode(Node root, int nodeIndex, Function<Node, Node> nodeOperation) {
    LinkedList<ReplaceableNode> openList = new LinkedList<>();
    openList.add(new ReplaceableNode(root));

    int totalSize = 0;
    while (openList.size() > 0) {
      ReplaceableNode node = openList.removeFirst();
      if (totalSize == nodeIndex) {
        // we are replacing this node
        Node replacement = nodeOperation.apply(node.getNode());
        return node.replace(replacement);
      }
      totalSize++;
      expand(node).forEach(openList::addLast);
    }
    return root;
  }

  public static List<ReplaceableNode> expand(ReplaceableNode node) {
    List<Node> children = node.getNode().getChildren();
    List<ReplaceableNode> replaceableNodes = new ArrayList<>(children.size());
    for (int i = 0; i < children.size(); i++) {
      replaceableNodes.add(new ReplaceableNode(node, i, children.get(i)));
    }
    return replaceableNodes;
  }
}
