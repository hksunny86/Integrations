package com.inov8.agentmate.model;

public class SourceOfFundsModel {

        public String id;
        public String name;

        public SourceOfFundsModel(String id, String name ) {
            this.id = id;
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setId(String id){ this.id = id;}
        public String getId(){ return id;}

        @Override
        public String toString() {
            return name;
        }
}