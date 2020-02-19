package kr.co.corners.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.corners.model.StationConfig;

@Component
public class StationConfigOperator {

	@Autowired
	StationConfigMapper dao;
	
	public StationConfig getStationConfig(int stationSn, int stationId, int siteId)
	{
		try {
			
			// SSR00010 화재, SSR00020 연기
			//Integer fireLevel = dao.getSensorLevel(stationId, siteId, "SSR00010");
			// Integer smokeLevel = dao.getSensorLevel(stationId, siteId, "SSR00020");
			
			String jsonString = dao.getDefaultAction(stationId, siteId);
			List<Integer> reportConfig = dao.getReportInfo(siteId);
			
			StationConfig sc = new StationConfig();			
			sc.setSid(stationSn);	
			byte cfgInterval = reportConfig.get(0).byteValue();		
			sc.setIntervalConf(cfgInterval);
			char cfgScale =  (char)reportConfig.get(1).byteValue();		
			sc.setScaleConf(cfgScale);
			byte option = reportConfig.get(2).byteValue();		
			sc.setOption(option);
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(jsonString);
			
			//{"tmp":70,"vol":0,"snd":0,"rpt":0,"blk":0,"ba":0,"lo":0,"lx":0,"br":0,"bl":0,"bu":0,"bd":0}
			byte[] datas = new byte[2];		
			int snd = node.get("snd").asInt();
			int rpt = node.get("rpt").asInt();
			int blk = node.get("blk").asInt();
			int ba = node.get("ba").asInt();
			int lo = node.get("lo").asInt();
			int lx = node.get("lx").asInt();
			int br = node.get("br").asInt();
			int bl = node.get("bl").asInt();
			int bu = node.get("bu").asInt();
			int bd = node.get("bd").asInt();
			
			datas[1] = (byte)(((blk << 7) & 0x80) + ((rpt << 6) & 0x40) + (snd & 0x3F));
			datas[0] = (byte)(((bd << 7) & 0x80) + (( bu << 6) & 0x40) + ((bl << 5) & 0x20) + 
					((br << 4) & 0x10) + ((lx << 3) & 0x08) + ((lo << 1) & 0x04) + (ba & 0x01));
	
			int action =  datas[1] & 0xFF + ((datas[0] & 0xFF ) >> 8);			
			sc.setDefaultAction(action);	
			
			int nTemp = node.get("tmp").asInt();
			sc.setTempConf(nTemp);
			
			int cfgVolume = node.get("vol").asInt();
			sc.setVolumeConf(cfgVolume);
			
			return sc;
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
