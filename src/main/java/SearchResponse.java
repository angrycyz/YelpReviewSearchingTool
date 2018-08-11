public class SearchResponse {

    private String user_id = new String();
    private String business_id = new String();
    private int stars = -1;
    private String date = new String();
    private String text = new String();
    private long useful = 0;
    private long funny = 0;
    private long cool = 0;
    private String city = new String();
    private String state = new String();
    private String postal = new String();
    private double user_score = 0;

    public SearchResponse(String user_id, String business_id, int stars, String date, String text, long useful, long funny, long cool, String city, String state, String postal, double user_score) {
        this.user_id = user_id;
        this.business_id = business_id;
        this.stars = stars;
        this.date = date;
        this.text = text;
        this.useful = useful;
        this.funny = funny;
        this.cool = cool;
        this.city = city;
        this.state = state;
        this.postal = postal;
        this.user_score = user_score;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public int getStars() {
        return stars;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public long getUseful() {
        return useful;
    }

    public long getFunny() {
        return funny;
    }

    public long getCool() {
        return cool;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUseful(long useful) {
        this.useful = useful;
    }

    public void setFunny(long funny) {
        this.funny = funny;
    }

    public void setCool(long cool) {
        this.cool = cool;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public double getUser_score() {
        return user_score;
    }

    public void setUser_score(double user_score) {
        this.user_score = user_score;
    }


}
