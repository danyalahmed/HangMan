package Java;

import java.util.ArrayList;
import java.util.List;

class myLists {
    private List<String> file1 = new ArrayList<>();
    private List<String> file2 = new ArrayList<>();
    private List<String> file3 = new ArrayList<>();
    private List<String> file4 = new ArrayList<>();

    public myLists() {
    }

    /**
     * @return the file
     */
    public List<String> getFile(int i) {
        switch (i + 1) {

            case 1:
                return file1;
            case 2:
                return file2;
            case 3:
                return file3;
            case 4:
                return file4;

            default:
                break;
        }
        return null;
    }

    /**
     * @param file the file to set
     */
    public void setFile(int i, List<String> file) {
        switch (i) {

            case 1:
                this.file1 = file;
            case 2:
                this.file2 = file;
            case 3:
                this.file3 = file;
            case 4:
                this.file4 = file;

            default:
                break;
        }
    }
}
