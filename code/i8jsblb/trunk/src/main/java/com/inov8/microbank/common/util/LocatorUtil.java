package com.inov8.microbank.common.util;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocatorUtil {


	private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

/*	public static ArrayList<BranchModel> getShortestDistanceByBranch(List<BranchModel> branches, double radius, double currentLatitude, double currentLongitude) {
		
		ArrayList<BranchModel> filteredBranchesList = new ArrayList<BranchModel>();
		for (BranchModel branchModel : branches) {
			if (branchModel.getGeoLocationModel()!=null) {
				double latitude = branchModel.getGeoLocationModel().getLatitude();
				double longitude = branchModel.getGeoLocationModel().getLongitude();
	
				double currentDistance = calculateDistance(currentLatitude, currentLongitude, latitude, longitude);
				if (currentDistance <= radius ) {
					currentDistance = Math.round(currentDistance * 100.0) / 100.0;
					branchModel.setDistanceDisplacement(currentDistance);
					filteredBranchesList.add(branchModel);
				}
			}
		}

		Collections.sort(filteredBranchesList, new Comparator<BranchModel>() {
			@Override
			public int compare(BranchModel o1, BranchModel o2) {
				Double b1 = Double.valueOf(o1.getDistanceDisplacement());
				Double b2 = Double.valueOf(o2.getDistanceDisplacement());

				return b1.compareTo(b2);
			}
		});

		return filteredBranchesList;
	}*/
	
/*	public static ArrayList<ATMModel> getShortestDistanceByATM(List<ATMModel> atms, double radius, double currentLatitude, double currentLongitude) {
		
		ArrayList<ATMModel> filteredAtmsList = new ArrayList<ATMModel>();
		for (ATMModel aTMModel : atms) {
			if (aTMModel.getGeoLocationModel()!=null) {
				double latitude = aTMModel.getGeoLocationModel().getLatitude();
				double longitude = aTMModel.getGeoLocationModel().getLongitude();

				double currentDistance = calculateDistance(currentLatitude, currentLongitude, latitude, longitude);
				if (currentDistance <= radius ) {
					aTMModel.setDistanceDisplacement(currentDistance);
					filteredAtmsList.add(aTMModel);
				}
			}
		}

		Collections.sort(filteredAtmsList, new Comparator<ATMModel>() {
			@Override
			public int compare(ATMModel o1, ATMModel o2) {
				Double b1 = Double.valueOf(o1.getDistanceDisplacement());
				Double b2 = Double.valueOf(o2.getDistanceDisplacement());

				return b1.compareTo(b2);
			}
		});

		return filteredAtmsList;
	}*/

	public static BaseWrapper filterMerchantsByDistance(List<RetailerContactDetailVO> models, double radius,
			double currentLatitude, double currentLongitude, int pageSize, int pageNo) {

			BaseWrapper baseWrapper = new BaseWrapperImpl();

			List<RetailerContactDetailVO> filteredModelList = new ArrayList<RetailerContactDetailVO>();
			if(CollectionUtils.isEmpty(models)) {
				baseWrapper.putObject(CommandFieldConstants.KEY_RETAILER_CONTACT_VO, (Serializable) filteredModelList);
				baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_COUNT, 0);
				return baseWrapper;
			}

			for(RetailerContactDetailVO model : models) {

				Double latitude = model.getLatitude();
				Double longitude = model.getLongitude();
				if(latitude == null || longitude == null)
					continue;

				double currentDistance = calculateDistance(currentLatitude, currentLongitude, latitude, longitude);
				if (currentDistance <= radius) {
					model.setDistanceDisplacement(currentDistance);
					filteredModelList.add(model);
				}
				/*if (filteredModelList.size() > (pageSize*pageNo) ) {
						break;
				}*/
			}
		Comparator<RetailerContactDetailVO> comparator = new Comparator<RetailerContactDetailVO>() {
			@Override
			public int compare(RetailerContactDetailVO o1,
							   RetailerContactDetailVO o2) {
				return o2.getDistanceDisplacement().compareTo( o1.getDistanceDisplacement());
			}
		};
			Collections.sort(filteredModelList, comparator);
		Collections.sort(filteredModelList,new GenericComparator("distanceDisplacement",Boolean.TRUE));

			int startIndex = (pageNo-1)*pageSize;
			int endIndex = ((pageNo-1)*pageSize)+pageSize;
			if (endIndex > filteredModelList.size()) {
				endIndex = filteredModelList.size();
			}
			/*if (startIndex==endIndex || startIndex>endIndex){
				filteredModelList = new ArrayList<>();
				baseWrapper.putObject(CommandFieldConstants.KEY_RETAILER_CONTACT_MODEL, (Serializable) filteredModelList);
				baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_COUNT, 0);
				return baseWrapper;
		}*/
			System.out.println("**************Start Index:"+startIndex +"-End Index:"+ endIndex+"**************");
			baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_COUNT, filteredModelList.size());
			List<RetailerContactDetailVO> subListFiltered = new ArrayList<>(filteredModelList.subList(startIndex, endIndex));
			baseWrapper.putObject(CommandFieldConstants.KEY_RETAILER_CONTACT_VO, (Serializable) subListFiltered );
			return baseWrapper;
	}
	
	public static double calculateDistance(Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude) {
		double currentDistance = distance( startLatitude, startLongitude, endLatitude, endLongitude);
		
		return Math.round(currentDistance * 100.0) / 100.0;
	}

	public static double distance(double startLat, double startLong, double endLat, double endLong) {

		double dLat = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));

		startLat = Math.toRadians(startLat);
		endLat = Math.toRadians(endLat);

		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}

	public static double BISPCalculateDistance(Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude) {
		double theta = startLongitude - endLongitude;
		double dist = Math.sin(Math.toRadians(startLatitude)) * Math.sin(Math.toRadians(endLatitude))
				+ Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;

		return Math.round(dist * 100.0) / 100.0;
	}

	public static double BISPAgentDistance(double startLat, double startLong, double endLat, double endLong) {

		double EARTH_RADIUS = 6.371; // Approx Earth radius in KM

		double dLat = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));

		startLat = Math.toRadians(startLat);
		endLat = Math.toRadians(endLat);

		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}
	public static double haversin(double val) {
		return Math.pow(Math.sin(val / 2), 2);
	}

}
