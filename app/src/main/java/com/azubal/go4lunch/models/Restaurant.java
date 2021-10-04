package com.azubal.go4lunch.models;

public class Restaurant {

       private String id;
         String name;
         String address;
         int rating;
         Boolean open;
         int distance;

        public Restaurant(String id, String name, String address, int rating, boolean open, int distance) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.rating = rating;
            this.open = open ;
            this.distance = distance;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }


        public int getRating() {
            return rating;
        }

        public Boolean getOpen() {
            return open;
        }

        public int getDistance() {
            return distance;
        }

}
