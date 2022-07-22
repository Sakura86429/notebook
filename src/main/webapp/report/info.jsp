<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-md-9">
    <div class="data_list"><%--class="glyphicon glyphicon-signal"借鉴了数据报表的图标--%>
        <div class="data_list_title"><span class="glyphicon glyphicon-signal"></span>&nbsp;数据报表</div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-12">
                    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
                    <%--准备一个柱状图的容器--%>
                    <div id="monthChart" style="height:500px; width: 100%"></div>

                    <!-- 百度地图的加载 -->
                    <h3 align="center">用户地区分布图</h3>
                    <%--百度地图的容器--%>
                    <div id="baiduMap" style="height: 600px; width: 100%"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 引入 ECharts 文件 -->
<script src="../statics/echarts/echarts.min.js"></script>
<%--引用百度地图API文件,需要申请百度地图对应ak密钥--%>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=odMf9zrQoudBkMiYG7c73lZwwIpoEgeu"></script>

<script type="text/javascript">
    /**
     * 通过月份查询云记数量
     */
    $.ajax({
       type:"get",
       url:"report",
       data:{
           actionName:"month"
       },
        //从后台拿到result
        success:function (result) {
            console.log(result);
            if(result.code == 1 ) {
                //得到月份（得到X轴的数据）
                var monthArray = result.result.monthArray;
                //得到月份对应的云记数量（得到Y轴的数据）
                var dataArray = result.result.dataArray;
                //加载柱状图
                loadMonthChart(monthArray, dataArray);
            }
        }
    });

    /**
     * 加载柱状图
     * @param monthArray
     * @param dataArray
     */
    function loadMonthChart(monthArray, dataArray) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('monthChart'));

        //X轴显示名称
        var dataAxis = monthArray;
        //Y轴的数据
        var data = dataArray;
        /*下面是生成左侧value值的*/
        //每个月的笔记并没有这么多，所以这里传30就足够了
        var yMax = 30;
        var dataShadow = [];

        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }

        var option = {
            //标题
            title: {
                text: '按月统计',   //主标题
                subtext: '通过月份查询对应的云记数量',   //副标题
                left:'center'   //标题对齐方式，center表示居中
            },
            //鼠标移动到到柱状图有提示框
            tooltip: {},
            /*legend:{
                data:['月份'],
            },*/
            //X轴
            xAxis: {
                data: dataAxis,
                //刻度
                axisTick: {
                    show: false
                },
                //线
                axisLine: {
                    show: false
                },
            },
            //Y轴
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            //系列
            series: [
                {   //For shadow
                    type: 'bar',   //bar柱状图
                    data: data,   //Y轴的数据
                    /*渐变色或者阴影效果*/
                    itemStyle: {
                        color: 'rgba(0,0,0,05)'
                    },
                    barGap: '-100%',
                    barCategoryGap: '40%',
                    data: dataShadow,
                    animation: false
                },
                {   //For shadow
                    type: 'bar',   //bar柱状图
                    data: data,   //Y轴的数据
                    //name: '月份',
                    /*渐变色或者阴影效果*/
                    /*showBackground: true,*/
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                {offset: 0, color: '#83bff6'},
                                {offset: 0.5, color: '#188df0'},
                                {offset: 1, color: '#188df0'}
                            ]
                        )
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#2378f7'},
                                    {offset: 0.7, color: '#2378f7'},
                                    {offset: 1, color: '#83bff6'}
                                ]
                            )
                        }
                    },
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    /**
     * 通过用户发布的坐标查询,后台传一个resultInfo回来给function里面的result
     */
    $.ajax({
        type:"get",
        url:"report",
        data:{
            actionName:"location"
        },
        success:function (result) {
            console.log(result);
            if(result.code == 1){
                //
                loadBaiduMap(result.result);
            }
        }
    });

    /**
     * 加载百度地图
     * @param markers
     */
    function loadBaiduMap(markers) {

        //加载地图实例（参数""要与容器的id对应）
        var map = new BMap.Map("baiduMap");
        //设置地图中心点
        var point = new BMap.Point(115.883254,28.746904);
        //地图初始化，同时设置地图展示级别
        //BMapGL.Map.centerAndZoom()方法要求设置中心点坐标和地图级别。
        //地图必须经过初始化才可以执行其他操作
        map.centerAndZoom(point, 15);

        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
        var scaleCtrl = new BMapGL.ScaleControl();  // 添加比例尺控件
        map.addControl(scaleCtrl);

        // // 创建点标记
        // var marker1 = new BMapGL.Marker(new BMapGL.Point(116.404, 39.925));
        // var marker2 = new BMapGL.Marker(new BMapGL.Point(116.404, 39.915));
        // var marker3 = new BMapGL.Marker(new BMapGL.Point(116.395, 39.935));
        // var marker4 = new BMapGL.Marker(new BMapGL.Point(116.415, 39.931));
        // // 在地图上添加点标记
        // map.addOverlay(marker1);
        // map.addOverlay(marker2);
        // map.addOverlay(marker3);
        // map.addOverlay(marker4);

        // //添加地图类型控件
        // map.addControl(new BMap.MapTypeControl({
        //     mapTypes:[
        //         BMAP_NORMAL_MAP,
        //         BMAP_HYBRID_MAP
        //     ]}));
        // map.setCurrentCity("南昌");          // 设置地图显示的城市 此项是必须设置的
        // map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

        //判断是否有点标记
        if(markers != null && markers.length >0){   //集合中第一个坐标时用户当前所在位置
            //将用户所在位置设置为中心点
            map.centerAndZoom(new BMap.Point(markers[0].lon,markers[0].lat),10);
            //循环在地图上添加点标记
            for (var i = 1; i < markers.length; i++) {
                // 创建点标记
                var marker = new BMap.Marker(new BMap.Point(markers[i].lon,markers[i].lat));
                // 在地图上添加点标记
                map.addOverlay(marker);
            }
        }
    }
</script>