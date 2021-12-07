package com.jkachele.pathfinding.enums;

public enum PathfindingID {
    A_STAR_CARDINAL {
        @Override
        public String toString() {
            return "A* Cardinal";
        }
    },
    A_STAR {
        @Override
        public String toString() {
            return "A*";
        }
    },
    DEPTH_FIRST_SEARCH {
        @Override
        public String toString() {
            return "DFS";
        }
    }
}
