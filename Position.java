package Project;

public enum Position {
    CB, FB, _6ER, _8ER, WIDE_PLAYER, CF;

    public String display() {
        switch (this) {
            case _6ER: return "6er";
            case _8ER: return "8er";
            case WIDE_PLAYER: return "Wide Player";
            default: return name();
        }
    }

    public static Position fromDisplay(String s) {
        if (s == null) return null;
        switch (s.trim().toLowerCase()) {
            case "6er": return _6ER;
            case "8er": return _8ER;
            case "wide player": return WIDE_PLAYER;
            case "cb": return CB;
            case "fb": return FB;
            case "cf": return CF;
            default: return null;
        }
    }
}