package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.X86ConfigInformationBean;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface X86ConfigInformationDao {
	List<X86ConfigInformationBean> getList(X86ConfigInformationBean tX86ConfigInformation);

	void create(X86ConfigInformationBean tX86ConfigInformation);

	int update(@Param("tX86ConfigInformationBeanList") List<X86ConfigInformationBean> tX86ConfigInformationBeanList);
	void updatescuState();
/*	X86ConfigInformationBean searchNewVersion();
	X86ConfigInformationBean selectx86bean();*/
}
