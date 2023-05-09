package human.search.controller.vo;

import lombok.Data;

import java.util.List;

@Data
public class DetectResult {
    private String bkgUrl;
    private List<String> faceUrls;
}
