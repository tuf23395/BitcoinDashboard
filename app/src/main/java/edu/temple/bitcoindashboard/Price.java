package edu.temple.bitcoindashboard;


    import android.content.res.Resources;

    import org.json.JSONException;
    import org.json.JSONObject;


    public class Price {

        private String name, symbol;
        private double price;

        public Price(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public Price (Resources r, JSONObject priceObject) throws JSONException{
            this(r.getString(R.string.currencyName), priceObject.getDouble("15m"));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public boolean equals(Object object){
            Price otherPrice = (Price) object;
            return this.symbol.equalsIgnoreCase(otherPrice.symbol);
        }

        public JSONObject getPriceAsJSON(){
            JSONObject priceObject = new JSONObject();
            try {
                priceObject.put("name", name);
                priceObject.put("price", price);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return priceObject;
        }

    }

