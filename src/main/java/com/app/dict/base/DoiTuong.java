package com.app.dict.base;

public class DoiTuong implements Comparable<DoiTuong>{
    private String searching;
    private String meaning;
    public DoiTuong(){
        searching = "";
        meaning = "";
    }

    public DoiTuong(String searching, String meaning) {
        this.searching = searching;
        this.meaning = meaning;
    }

    public String getSearching() {
        return searching;
    }

    public void setSearching(String searching) {
        this.searching = searching;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public int compareTo(DoiTuong o) {
        return (generalizeVietnameseString(getSearching()).compareTo(o.getSearching()));
    }

    public static String generalizeVietnameseString(String vietnameseString) {
        // Remove accents
        String normalizedString = Normalizer.normalize(vietnameseString, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutAccents = pattern.matcher(normalizedString).replaceAll("");

        // Convert to lowercase
        String lowercaseString = withoutAccents.toLowerCase();

        // Remove redundant spaces
        String trimmedString = lowercaseString.trim();
        String generalizedString = trimmedString.replaceAll("\\s+", " ");

        return generalizedString;
    }
}
