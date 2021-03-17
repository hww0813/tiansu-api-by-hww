package com.yuanqing.project.tiansu.domain.report;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author xucan
 * @version 1.0
 */
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
