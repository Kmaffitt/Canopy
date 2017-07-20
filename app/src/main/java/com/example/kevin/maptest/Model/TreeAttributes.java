package com.example.kevin.maptest.Model;


public class TreeAttributes {
    public enum treeWidth{
        NARROW("narrow"), MEDIUM("medium"), WIDE("wide");
        String descriptor;

        treeWidth(String s){
            this.descriptor = s;
        }
        public String getDescriptor(){
            return this.descriptor;
        }
    }
    public enum treeDistance {
        TENELEVEN("10-11"), TWELVETHIRTEEN("12-13"), FOURTEENFIFTEEN("14-15"), SIXTEENSEVENTENN("16-17"), EIGHTEENNINETEEN("18-19");
        String descriptor;

        treeDistance(String s) {
            this.descriptor = s;
        }

        public String getDescriptor() {
            return this.descriptor;
        }
    }

    private int treeDistance;
    private int treeWidth;

    public TreeAttributes(int width, int dist){
        this.treeWidth = width;
        this. treeDistance = dist;
    }

    public void setTreeDistance(int dist){
        this.treeDistance = dist;
    }
    public int getTreeDistance(){
        return this.treeDistance;
    }
    public void setTreeWidth(int width){
        this.treeWidth = width;
    }
    public int getTreeWidth(){
        return this.treeWidth;
    }
}
