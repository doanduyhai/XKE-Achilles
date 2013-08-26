package info.archinnov.achilles.demo.music.constants;


public enum Rating {

    ZERO(0L),
    ONE(1L),
    ONE_AND_HALF(2L),
    TWO(3L),
    TWO_AND_HALF(4L),
    THREE(5L),
    THREE_AND_HALF(6L),
    FOUR(7L),
    FOUR_AND_HALF(8L),
    FIVE(9L);

    private final long ratingValue;

    private Rating(long ratingValue)
    {
        this.ratingValue = ratingValue;
    }

    public long ratingValue()
    {
        return ratingValue;
    }
}
