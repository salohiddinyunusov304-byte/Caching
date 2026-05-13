package uz.pdp.caching.payload;

public record PostUpdater(
        Integer id,
        Integer userId,
        String title,
        String body
) {
}
