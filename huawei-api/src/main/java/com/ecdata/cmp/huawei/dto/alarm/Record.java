package com.ecdata.cmp.huawei.dto.alarm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "告警信息", description = "告警信息")
public class Record implements Serializable {
    private static final long serialVersionUID = -4628427066589452894L;

    @ApiModelProperty(value = "主键，网管告警流水号")
    private long csn;

    @ApiModelProperty(value = "区分告警事件")
    private int category;

    @ApiModelProperty(value = "变更版本号")
    private int version;

    @ApiModelProperty(value = "告警变更的类型")
    private int changeFlag;

    @ApiModelProperty(value = "告警发生次数。该字段仅供内部使用")
    private int count;

    @ApiModelProperty(value = "归并告警的组标识。该字段仅供内部使用")
    private long mergeGroupId;

    @ApiModelProperty(value = "是否为被归并告警。该字段仅供内部使用")
    private int merged;

    @ApiModelProperty(value = "告警清除状态")
    private int cleared;

    @ApiModelProperty(value = "告警清除操作员")
    private String clearUser;

    @ApiModelProperty(value = "告警清除UTC时间")
    private long clearUtc;

    @ApiModelProperty(value = "告警清除的网元本地时间")
    private long clearTime;

    @ApiModelProperty(value = "告警清除时间的网元夏令时偏移")
    private long clearDst;

    @ApiModelProperty(value = "告警是否可自动清除")
    private int clearCategory;

    @ApiModelProperty(value = "告警是否确认")
    private int acked;

    @ApiModelProperty(value = "告警确认操作员")
    private String ackUser;

    @ApiModelProperty(value = "确认/反确认UTC时间")
    private long ackUtc;

    @ApiModelProperty(value = "告警首次发生的UTC时间。该字段仅供内部使用")
    private long firstOccurUtc;

    @ApiModelProperty(value = "告警首次发生的网元本地时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long firstOccurTime;

    @ApiModelProperty(value = "告警首次发生时间的网元夏令时偏移")
    private long firstOccurDst;

    @ApiModelProperty(value = "告警发生的UTC时间")
    private long occurUtc;

    @ApiModelProperty(value = "告警发生的网元本地时间")
    private long occurTime;

    @ApiModelProperty(value = "告警发生时间的网元夏令时偏移")
    private long occurDst;

    @ApiModelProperty(value = "告警到达网管的UTC时间")
    private long arriveUtc;

    @ApiModelProperty(value = "告警最后发生的UTC时间")
    private long latestOccurUtc;

    @ApiModelProperty(value = "告警最后发生的网元本地时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long latestOccurTime;

    @ApiModelProperty(value = "告警最后发生时间的网元夏令时偏移")
    private long latestOccurDst;

    @ApiModelProperty(value = "告警重复/清除匹配Key")
    private String matchKey;

    @ApiModelProperty(value = "告警归并匹配key")
    private String mergeKey;

    @ApiModelProperty(value = "Driver层的资源标识")
    private String nativeMeDn;

    @ApiModelProperty(value = "下级资源标识")
    private String nativeMoDn;

    @ApiModelProperty(value = "下级资源名称")
    private String nativeMoName;

    @ApiModelProperty(value = "Driver层的资源名称")
    private String nativeMeName;

    @ApiModelProperty(value = "告警对应的对象标识")
    private String meDn;

    @ApiModelProperty(value = "告警对应的对象名称")
    private String meName;

    @ApiModelProperty(value = "对象类型")
    private String moc;

    @ApiModelProperty(value = "网元大类")
    private String meCategory;

    @ApiModelProperty(value = "网元类别")
    private String meType;

    @ApiModelProperty(value = "网元类型")
    private String productName;

    @ApiModelProperty(value = "告警分组标识")
    private String alarmGroupId;

    @ApiModelProperty(value = "告警来源系统类型")
    private String originSystemType;

    @ApiModelProperty(value = "来源系统内部标识")
    private String originSystemId;

    @ApiModelProperty(value = "来源系统外部标识")
    private String originSystem;

    @ApiModelProperty(value = "来源系统名称")
    private String originSystemName;

    @ApiModelProperty(value = "所属租户名称")
    private String tenant;

    @ApiModelProperty(value = "所属租户标识")
    private String tenantId;

    @ApiModelProperty(value = "区域标识")
    private String regionId;

    @ApiModelProperty(value = "分片标识")
    private int shardId;

    @ApiModelProperty(value = "区域名称")
    private String region;

    @ApiModelProperty(value = "数据中心标识")
    private String dcId;

    @ApiModelProperty(value = "数据中心名称")
    private String dcName;

    @ApiModelProperty(value = "服务组标识")
    private String svcGroupId;

    @ApiModelProperty(value = "厂商")
    private String manufacturer;

    @ApiModelProperty(value = "制式")
    private String domain;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "对象标识")
    private String moi;

    @ApiModelProperty(value = "备份状态")
    private int backupStatus;

    @ApiModelProperty(value = "告警状态标识")
    private int identifier;

    @ApiModelProperty(value = "告警设备流水号")
    private int subCsn;

    @ApiModelProperty(value = "事件类型")
    private int eventType;

    @ApiModelProperty(value = "告警标识")
    private String alarmId;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

    @ApiModelProperty(value = "告警级别")
    private int severity;

    @ApiModelProperty(value = "可能原因描述")
    private String probableCause;

    @ApiModelProperty(value = "告警具体原因标识")
    private int reasonId;

    @ApiModelProperty(value = "影响业务标识")
    private int serviceAffectedType;

    @ApiModelProperty(value = "告警所影响的业务名称")
    private String affectedService;

    @ApiModelProperty(value = "根源告警流水号")
    private String rootCsn;

    @ApiModelProperty(value = "设备根源告警")
    private String subRootCsn;

    @ApiModelProperty(value = "附加信息")
    private String additionalInformation;

    @ApiModelProperty(value = "扩展字段")
    private String userData;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "工程告警标记")
    private int specialAlarmStatus;

    @ApiModelProperty(value = "关联告警组标识，高效派工单特性使用")
    private String corrGroupId;

    @ApiModelProperty(value = "工单号")
    private String workOrderId;

    @ApiModelProperty(value = "派单用户")
    private String workOrderSender;

    @ApiModelProperty(value = "派单的UTC时间")
    private long workOrderUtc;

    @ApiModelProperty(value = "工单状态")
    private String workOrderStatus;

    @ApiModelProperty(value = "告警是否设置为无效")
    private int invalidated;

    @ApiModelProperty(value = "告警处理人")
    private String ownerUID;

    @ApiModelProperty(value = "告警汇聚组标识")
    private String aggrGroupId;

    @ApiModelProperty(value = "汇聚告警流水号")
    private String aggrRootCsn;

    @ApiModelProperty(value = "告警汇聚状态")
    private int aggrStatus;

    @ApiModelProperty(value = "告警匹配规则")
    private String ruleName;

    @ApiModelProperty(value = "可用区标识")
    private String azoneId;

    @ApiModelProperty(value = "可用区")
    private String azoneName;

    @ApiModelProperty(value = "逻辑位置标识")
    private String logicalRegionId;

    @ApiModelProperty(value = "逻辑位置")
    private String logicalRegionName;

    @ApiModelProperty(value = "NFV资源池标识")
    private String resPoolId;

    @ApiModelProperty(value = "NFV资源池")
    private String resPoolName;

    @ApiModelProperty(value = "自定义资源分组标识")
    private String resGroupId;

    @ApiModelProperty(value = "组织标识")
    private String vdcId;

    @ApiModelProperty(value = "组织")
    private String vdcName;

    @ApiModelProperty(value = "设备类型标识")
    private String deviceTypeId;

    @ApiModelProperty(value = "告警清除类型")
    private int clearType;

    @ApiModelProperty(value = "确认时间夏令时偏移量")
    private long ackDst;

    @ApiModelProperty(value = "告警在告警源是否备份")
    private int backedupOnSource;

    @ApiModelProperty(value = "CK相关性机制字段")
    private int ckSource;

    @ApiModelProperty(value = "定位信息字典分隔符标志")
    private int locParseFlag;

    @ApiModelProperty(value = "修改备注时间")
    private int commentUtc;

    @ApiModelProperty(value = "修改备注用户")
    private String commentUser;

    @ApiModelProperty(value = "告警变更字段集合")
    private List<String> changedFields;

    @ApiModelProperty(value = "资源分组子网ID")
    private String domainSubnetId;

    @ApiModelProperty(value = "上报北向标识")
    private int reportNorth;

    @ApiModelProperty(value = "清除匹配策略")
    private int clearMatchStrategy;

    @ApiModelProperty(value = "moDn字段（uuid）")
    private String moDn;

    @ApiModelProperty(value = "修改时间")
    private long endUtc;

    @ApiModelProperty(value = "告警扩展字段map")
    private ExtParams extParams;
}
