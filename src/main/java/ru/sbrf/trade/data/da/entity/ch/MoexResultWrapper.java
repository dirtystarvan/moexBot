package ru.sbrf.trade.data.da.entity.ch;

public class MoexResultWrapper {
	MoexDto[] growthLeaders;
	MoexDto[] fallLeaders;

	public MoexDto[] getGrowthLeaders() {
		return growthLeaders;
	}

	public void setGrowthLeaders(MoexDto[] growthLeaders) {
		this.growthLeaders = growthLeaders;
	}

	public MoexDto[] getFallLeaders() {
		return fallLeaders;
	}

	public void setFallLeaders(MoexDto[] fallLeaders) {
		this.fallLeaders = fallLeaders;
	}
}
