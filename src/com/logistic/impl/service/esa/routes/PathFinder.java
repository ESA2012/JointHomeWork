package com.logistic.impl.service.esa.routes;

import com.logistic.api.model.post.Package;
import com.logistic.api.model.transport.DeliveryTransport;
import com.logistic.impl.model.post.PostOfficeImproved;
import com.logistic.impl.service.DataStorage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by SnakE on 11.11.2015.
 */
public class PathFinder {
    private boolean[] marked;
    private Deque<Integer> queue;
    private int[] nodeLast;
    private int depNode;
    private int nodesCount;
    private RouteMatrix matrix;

    public PathFinder(int startNode, Package parcel, int[] types) {
        this.matrix = DataStorage.getRouteMatrix();
        this.nodesCount = matrix.getSize();
        this.marked = new boolean[nodesCount];
        this.queue = new ArrayDeque<>();
        this.nodeLast = new int[nodesCount];
        this.depNode = startNode;
        findPaths(DataStorage.getPostOffices(), parcel, types);
    }


    private boolean isInArray(int i, int[] a) {
        boolean res = false;
        for (int n: a) {
            if (i == n) {
                res = true;
                break;
            }
        }
        return res;
    }


    private void findPaths(List<PostOfficeImproved> postOffices, Package parcel, int[] types) {
        marked[depNode] = true;
        queue.add(depNode);
        while (queue.peek() != null) {
            int node1 = queue.pollFirst();
            for (int node2 = 0; node2 < nodesCount; node2++) {
                if (isInArray(matrix.isConnected(node1,node2),types)) {
                    if (postOffices.get(node2).getAcceptablePackageTypes().contains(parcel.getType())) {
                        if (!marked[node2]) {
                            marked[node2] = true;
                            nodeLast[node2] = node1;
                            queue.add(node2);
                        }
                    }
                }
            }
        }
    }


    public List<Integer> findPath (int destNode) {
        if (hasWayTo(destNode)) {
            List<Integer> route = new LinkedList<>();
            for (int x = destNode; x != depNode; x = nodeLast[x]) {
                route.add(0, x);
            }
            route.add(0, depNode);
            return route;
        } else {
            return null;
        }
    }


    public boolean hasWayTo(int destination) {
        if (marked[destination]) {
            return true;
        } else {
            return false;
        }
    }

}
