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

    public String ratingString()
    {
        switch (this)
        {
            case ZERO:
                return "00";
            case ONE:
                return "01";
            case ONE_AND_HALF:
                return "02";
            case TWO:
                return "03";
            case TWO_AND_HALF:
                return "04";
            case THREE:
                return "05";
            case THREE_AND_HALF:
                return "06";
            case FOUR:
                return "07";
            case FOUR_AND_HALF:
                return "08";
            case FIVE:
                return "09";
            default:
                return "00";
        }
    }
}
