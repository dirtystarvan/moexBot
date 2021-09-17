package ru.sbrf.trade.data.util;

import ru.sbrf.trade.data.da.entity.ch.MoexDto;
import ru.sbrf.trade.data.da.entity.ch.MoexResultWrapper;

import java.util.List;

public class MsgBuilder {
	private static final String HEAD = "ТИКЕР Open/High/Low/Close\n";

    public static String getMessage(List<MoexDto> list, String date) {
    	StringBuilder builder = new StringBuilder();
    	builder.append(date).append("\n");

//		2021-03-20
//		ТИКЕР Open/High/Low/Close
//		ALRS 95.05/97.3/94.6/96.1
//		ROSN 438.05/433.1/444.6/440. 8

		builder.append(HEAD);

		for (MoexDto dto: list) {
			builder.append(dto.secid).append(" ")
					.append(dto.open).append("/")
					.append(dto.high).append("/")
					.append(dto.low).append("/")
					.append(dto.close);
			builder.append("\n");
		}

    	return builder.toString();
	}

	public static String getDayAnalysisMessage(MoexResultWrapper result) {
    	if (result == null) {
    		return "Something went wrong.... ¯\\_(ツ)_/¯";
		}

		StringBuilder builder = new StringBuilder();

		builder.append("Лидеры роста:").append("\n\n");
		builder.append(HEAD);
		for (MoexDto dto: result.getGrowthLeaders()) {
			builder.append(dto.secid).append(" ")
					.append(dto.open).append("/")
					.append(dto.high).append("/")
					.append(dto.low).append("/")
					.append(dto.close);
			builder.append("\n");
		}

		builder.append("\n");

		builder.append("Лидеры падения:").append("\n\n");
		builder.append(HEAD);
		for (MoexDto dto: result.getFallLeaders()) {
			builder.append(dto.secid).append(" ")
					.append(dto.open).append("/")
					.append(dto.high).append("/")
					.append(dto.low).append("/")
					.append(dto.close);
			builder.append("\n");
		}

		return builder.toString();
	}
}
