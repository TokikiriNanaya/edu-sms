package com.caizi.edu.sms.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "视图-消息通知")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VOMsgInfo implements Serializable {

    private static final long serialVersionUID = 2344182269077703057L;

    @ApiModelProperty(value = "待办菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "待办数量")
    private int needInspNum;
}
