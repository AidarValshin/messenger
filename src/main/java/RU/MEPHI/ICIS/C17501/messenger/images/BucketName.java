package RU.MEPHI.ICIS.C17501.messenger.images;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {

    USER_IMAGE("messenger-images"),
    CHAT_IMAGE("chat-images");

    private final String bucketName;
}
