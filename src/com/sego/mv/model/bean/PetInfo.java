package com.sego.mv.model.bean;

import java.util.ArrayList;
import java.util.List;

import com.richitec.util.JSONUtil;

public class PetInfo {
	private String petid;
	private String nickname;

	public String getPetid() {
		return petid;
	}

	public void setPetid(String petId) {
		this.petid = petId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public static class PetInfos {
		java.util.List<PetInfo> list;

		public java.util.List<PetInfo> getList() {
			return list;
		}

		public void setList(java.util.List<PetInfo> list) {
			this.list = list;
		}
		
	}

	public static void main(String[] args) {
		// String json =
		// "{\"list\": [{\"petid\" : \"123\", \"nickname\" : \"kkk\" }]}";
		// PetInfo petInfo = JSONUtil.toObject(json, PetInfo.class);
		// System.out.print("petId: " + petInfo.getPetid() + " nickname: " +
		// petInfo.getNickname());

		List<PetInfo> list = new ArrayList<PetInfo>();
		PetInfo petInfo = new PetInfo();
		petInfo.setPetid("12");
		petInfo.setNickname("sdf");
		list.add(petInfo);
		
		petInfo = new PetInfo();
		petInfo.setPetid("333");
		petInfo.setNickname("asdfasdf");
		list.add(petInfo);
		PetInfos petInfos = new PetInfos();
		petInfos.setList(list);
		
		System.out.println(JSONUtil.toString(petInfos));
	}
}
