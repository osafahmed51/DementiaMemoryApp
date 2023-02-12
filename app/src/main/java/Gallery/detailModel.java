package Gallery;

public class detailModel {
    String description,dateTime,image;

    public detailModel() {
    }

    public detailModel(String description, String dateTime, String image) {
        this.description = description;
        this.dateTime = dateTime;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
