package com.zc.streaming;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.thoughtworks.xstream.XStream;
import com.zc.async.AsyncEmpENbrRequest;
import com.zc.async.AsyncEmpPersonIdRequest;
import com.zc.async.AsyncFlihgtInfoRequest;
import com.zc.common.SnowflakeIdWorker;
import com.zc.component.XStreamGen;
import com.zc.model.*;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FlihgtOprCrew {

    public static final String SYSTEMCODE = "MU";

    public static void main(String[] args) throws Exception {


        Class.forName("oracle.jdbc.driver.OracleDriver");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        final RMQConnectionConfig rmqConf = new RMQConnectionConfig.Builder()
//                .setHost("")
//                .setPort(5672)
//                .setUserName("camel")
//                .setPassword("camel123")
//                .setVirtualHost("/flight")
//                .build();
//
//        env.addSource(new RMQSource<>(
//                "two.flt.crew",
//                true,
//        ))

//        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CrewRoot><SysInfo><MessageSequenceID>43e09e68-b21a-47c5-a61c-b3de35ab9d0d</MessageSequenceID><ServiceType>IbmMq</ServiceType><MessageType>XML</MessageType><SendDateTime>2018-11-14 11:00:21</SendDateTime><CreateDateTime>2018-11-14 11:00:21</CreateDateTime></SysInfo><FlightInfo><Flight_Id>12517054</Flight_Id><Mm_Leg_Id>128943836</Mm_Leg_Id><Flight_Date>2018-11-14</Flight_Date><Carrier>MU</Carrier><Flight_No>MU5467</Flight_No><Departure_Airport>PVG</Departure_Airport><Arrival_Airport>HFE</Arrival_Airport><Crew_Link_Line>16699549</Crew_Link_Line><Ac_Type>320,A320</Ac_Type><Flight_Flag>A</Flight_Flag><Op_Time>2018-11-14 00:01:13</Op_Time><Weather_Standard/></FlightInfo><CrewInfo><PolitInfo><Flight_Id>12517054</Flight_Id><Fxw_Id>35392</Fxw_Id><Org_Code>EA10010202</Org_Code><Post>42</Post><Prank>J(0.24)</Prank><Staff_Code>15104</Staff_Code><Staff_Name>张浩</Staff_Name><Eng_Surname>ZHANG</Eng_Surname><Eng_Name>HAO</Eng_Name><License_No>274b977e1c</License_No><Operate>Delete</Operate><FirstLaunch>false</FirstLaunch><Board_Card_No/><Passport_Code/><Visa_Code/><Sex>M</Sex><Birthday>1959-05-08</Birthday><Nationality>中国</Nationality><Mobile_Tel>13761554422</Mobile_Tel><P_Code>2921</P_Code><Id_No>65010419590508331X</Id_No><Rank_No>S004</Rank_No><Rank_Name>航班任务搭机</Rank_Name><Rec_Id>23150770</Rec_Id><Fjs_Order>10</Fjs_Order><Ts_Flag/><Op_Time>2018-11-07 11:06:14</Op_Time></PolitInfo><PolitInfo><Flight_Id>12517055</Flight_Id><Fxw_Id>35391</Fxw_Id><Org_Code>EA10010202</Org_Code><Post>42</Post><Prank>J(0.24)</Prank><Staff_Code>15104</Staff_Code><Staff_Name>张男</Staff_Name><Eng_Surname>ZHANG</Eng_Surname><Eng_Name>HAO</Eng_Name><License_No>65010419590508331X</License_No><Operate>Delete</Operate><FirstLaunch>false</FirstLaunch><Board_Card_No/><Passport_Code/><Visa_Code/><Sex>M</Sex><Birthday>1959-05-08</Birthday><Nationality>中国</Nationality><Mobile_Tel>13761554422</Mobile_Tel><P_Code>2921</P_Code><Id_No>65010419590508331X</Id_No><Rank_No>S004</Rank_No><Rank_Name>航班任务搭机</Rank_Name><Rec_Id>23150770</Rec_Id><Fjs_Order>10</Fjs_Order><Ts_Flag/><Op_Time>2018-11-07 11:06:14</Op_Time></PolitInfo></CrewInfo></CrewRoot>\n";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CrewRoot><SysInfo><MessageSequenceID>43e09e68-b21a-47c5-a61c-b3de35ab9d0d</MessageSequenceID><ServiceType>IbmMq</ServiceType><MessageType>XML</MessageType><SendDateTime>2018-11-14 11:00:21</SendDateTime><CreateDateTime>2018-11-14 11:00:21</CreateDateTime></SysInfo><FlightInfo><Flight_Id>12517054</Flight_Id><Mm_Leg_Id>128943836</Mm_Leg_Id><Flight_Date>2018-11-14</Flight_Date><Carrier>MU</Carrier><Flight_No>MU5467</Flight_No><Departure_Airport>PVG</Departure_Airport><Arrival_Airport>HFE</Arrival_Airport><Crew_Link_Line>16699549</Crew_Link_Line><Ac_Type>320,A320</Ac_Type><Flight_Flag>A</Flight_Flag><Op_Time>2018-11-14 00:01:13</Op_Time><Weather_Standard/></FlightInfo><CrewInfo><PolitInfo><Flight_Id>12517054</Flight_Id><Fxw_Id>35392</Fxw_Id><Org_Code>EA10010202</Org_Code><Post>42</Post><Prank>J(0.24)</Prank><Staff_Code>15104</Staff_Code><Staff_Name>张浩</Staff_Name><Eng_Surname>ZHANG</Eng_Surname><Eng_Name>HAO</Eng_Name><License_No>274b977e1c</License_No><Operate>Delete</Operate><FirstLaunch>false</FirstLaunch><Board_Card_No/><Passport_Code/><Visa_Code/><Sex>M</Sex><Birthday>1959-05-08</Birthday><Nationality>中国</Nationality><Mobile_Tel>13761554422</Mobile_Tel><P_Code>2921</P_Code><Id_No>65010419590508331X</Id_No><Rank_No>S004</Rank_No><Rank_Name>航班任务搭机</Rank_Name><Rec_Id>23150770</Rec_Id><Fjs_Order>10</Fjs_Order><Ts_Flag/><Op_Time>2018-11-07 11:06:14</Op_Time></PolitInfo></CrewInfo></CrewRoot>";
        SingleOutputStreamOperator<Tuple2<FlightInfo, PolitInfo>> dataInput = env.fromElements(xml)

                .map(new MapFunction<String, CrewRoot>() {
                    @Override
                    public CrewRoot map(String s) throws Exception {
//                        XStream xStream = new XStream(new DomDriver());
//                        xStream.processAnnotations(CrewRoot.class);
//                        xStream.autodetectAnnotations(true);
                        final XStream xStream = XStreamGen.getInstance();
                        CrewRoot cr = (CrewRoot) xStream.fromXML(s);
                        return cr;
                    }
                })
                //拆分到人员
                .flatMap(new FlatMapFunction<CrewRoot, Tuple2<FlightInfo, PolitInfo>>() {
                    @Override
                    public void flatMap(CrewRoot crewRoot, Collector<Tuple2<FlightInfo, PolitInfo>> collector) throws Exception {
                        for (PolitInfo polit : crewRoot.getCrewInfo().getPolitInfos()) {
                            collector.collect(Tuple2.of(crewRoot.getFlightInfo(), polit));
                        }
                    }
                });
        //航班信息
        final SingleOutputStreamOperator<Tuple2<PolitInfo, FlightCrewMsg>> dataWithFlight = AsyncDataStream.unorderedWait(
                dataInput,
                new AsyncFlihgtInfoRequest(
                        "",
                        "",
                        ""),
                5,
                TimeUnit.SECONDS,
                100
        ).map(new MapFunction<Tuple2<FlightInfoPo, PolitInfo>, Tuple2<PolitInfo, FlightCrewMsg>>() {
            @Override
            public Tuple2<PolitInfo, FlightCrewMsg> map(Tuple2<FlightInfoPo, PolitInfo> in) {
                FlightCrewMsg msg = new FlightCrewMsg();
                msg.setAIRCRAFT_REGISTER_ID(in.f0.getAircraftTailNo());
                msg.setFLIGHT_NO(in.f0.getFlightNo());
                msg.setSTA_UTC(in.f0.getStaUtc());
                msg.setSTD_UTC(in.f0.getStdUtc());
                msg.setPLAN_DEST_AIRPORT_CD(in.f0.getPlanDestAirportCd());
                msg.setPLAN_ORIG_AIRPORT_CD(in.f0.getPlanOrigAirportCd());
//                msg.setFLIGHT_DATE(in.f0.getf);
                msg.setSTAFF_CODE(in.f1.getStaff_Code());
                msg.setORG_CODE(in.f1.getOrg_Code());
                return Tuple2.of(in.f1, msg);
            }
        });
        dataWithFlight.print();

        //通过身份证号查person_id
        final SingleOutputStreamOperator<Tuple3<PolitInfo, FlightCrewMsg, EmpInfoPo>> dataWithFlihgtPersonId = AsyncDataStream.unorderedWait(
                dataWithFlight,
                new AsyncEmpPersonIdRequest(
                        "",
                        "",
                        ""),
                5,
                TimeUnit.SECONDS,
                100
        );

        dataWithFlihgtPersonId.print();

        final SingleOutputStreamOperator<Tuple2<FlightCrewMsg, EmpInfoPo>> dataWithFlihgtPerson = dataWithFlihgtPersonId.map(new MapFunction<Tuple3<PolitInfo, FlightCrewMsg, EmpInfoPo>, Tuple2<FlightCrewMsg, EmpInfoPo>>() {
            @Override
            public Tuple2<FlightCrewMsg, EmpInfoPo> map(Tuple3<PolitInfo, FlightCrewMsg, EmpInfoPo> input) throws Exception {
                input.f1.setORG_CODE(input.f1.getORG_CODE());
                input.f1.setSTAFF_CODE(input.f0.getStaff_Code());
                return new Tuple2<FlightCrewMsg, EmpInfoPo>(input.f1, input.f2);
            }
        });
        dataWithFlihgtPerson.print();
        //person_id --> emp_num
        final SingleOutputStreamOperator<Tuple2<FlightCrewMsg, EmpInfoPo>> dataAll = AsyncDataStream.unorderedWait(
                dataWithFlihgtPerson,
                new AsyncEmpENbrRequest(
                        "",
                        "",
                        ""),
                5,
                TimeUnit.SECONDS,
                100
        );

        final SingleOutputStreamOperator<String> out = dataAll.map(new MapFunction<Tuple2<FlightCrewMsg, EmpInfoPo>, FlightCrewMsg>() {
            @Override
            public FlightCrewMsg map(Tuple2<FlightCrewMsg, EmpInfoPo> input) throws Exception {
//                input.f0.setStaffCode(input.f1.getStaffCode());
//                input.f0.setOrgCode(input.f1.getOrgCode());
                input.f0.setEMPLOYEE_NUMBER(input.f1.getEmployeeNumber());
                input.f0.setSYSTEMCODE(SYSTEMCODE);
                input.f0.setSEQUENCE(String.valueOf(new SnowflakeIdWorker(1, 1).nextId()));
                input.f0.setSENDTIME(new Date());
                return input.f0;
            }
        })
                .map(f ->
                {
                    return JSONObject.toJSONString(f, SerializerFeature.WriteMapNullValue);
                });
        out.print();

        env.execute("FLightOprCrew");

    }
}
