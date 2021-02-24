package com.yuanqing.project.tiansu.domain.video;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitRate {
   private Long cityCode;
   private String cityName;
   private Long cameraCnt;
   private String rate;
   private Long visitCnt;
   private Long clientCnt;
   private Long visitedCnt;

}
