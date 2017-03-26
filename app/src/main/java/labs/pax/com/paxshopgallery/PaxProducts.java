package labs.pax.com.paxshopgallery;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class PaxProducts {
    @JsonField
    public List<Product> pods = new ArrayList<>();

    @JsonObject
    public static class Product {
        @JsonField(name="id")
        public String id;
        @JsonField(name="name")
        public String name;
        @JsonField(name="description")
        public String description;
        @JsonField(name="price")
        public int price;
        @JsonField(name="thumbnail_url")
        public String thumbnailURL;
        @JsonField(name="image_url")
        public String imageURL;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(PaxProducts.class.getSimpleName());
            sb.append(", id = ");
            sb.append(id);
            sb.append(", name = ");
            sb.append(name);
            sb.append(", description = ");
            sb.append(description);
            sb.append(", price = ");
            sb.append(price);
            sb.append(", thumbnailURL = ");
            sb.append(thumbnailURL);
            sb.append(", imageURL = ");
            sb.append(imageURL);
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return pods.toString();
    }
}
