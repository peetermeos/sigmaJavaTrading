package sigma.trading;


import java.util.List;
import java.util.Set;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types.SecType;

import sigma.utils.Helper;


/**
 * TWS Connector for volatility surface mapping
 * @author Peeter Meos
 *
 */
public class VolConnector extends TwsConnector {
	
	private Contract inst;
	private int underConID = -1;
	

	/**
	 * Constructs VolConnector instance that does the TWS connection
	 * and information retrieval.
	 */
	public VolConnector() {
		super("TWS Volatility Optimizer Connector");
	}
	
	/**
	 * Creates contract for the full instrument option chain
	 */
	public void createContract(Instrument hedgeInst) {
		logger.log("Creating contract");
		
		inst = new Contract();
		
		inst.symbol("CL");
		inst.exchange("NYMEX");
		inst.currency("USD");

		//inst.lastTradeDateOrContractMonth("201710");
		inst.secType(SecType.FUT);
	}
	
	/**
	 * Retrieves portfolio info
	 */
	public void retrievePortfolio() {
		logger.log("Retrieving active portfolio");
		if (tws.isConnected()) {
			tws.reqPositions();
		}
	}
	
	/**
	 * Requests option chain contract details
	 */
	public void getOptionChain(List<Instrument> hedgeInst) {
		// TODO option chain parameters need to be added.
		// Such as - range of strikes and expiries
		// or reqSecDefOptParams
		
		logger.log("Requesting contract details for underlying");
		if (inst != null && tws.isConnected()) {
			
			tws.reqContractDetails(nextOrderID, inst);
			// wait until we have contract ID for underlying
			logger.log("Waiting for underlying contract ID");
			while (underConID < 0) {
				Helper.sleep(100);
			}
			
			//tws.reqMktData(tickerId, contract, genericTickList, snapshot, mktDataOptions);
			
			logger.log("Contract ID received, proceeding.");
			logger.log("Retrieving option chain");
			//tws.reqSecDefOptParams(nextOrderID, inst.symbol(), inst.exchange(), inst.secType().toString(), underConID);
		}
	}
	
	/**
	 * 
	 */
	public void calculateGreeks() {
		logger.log("Calculating greeks");
	}
	
	
	/**
	 * 
	 */
	public void createOrders() {
		logger.log("Creating orders");
	}
	
	@Override
    public void position(String account, Contract contract, double pos,
            double avgCost) {
        logger.log("Position. " + account+
        		" - Symbol: " + contract.symbol() +
        		", SecType: " + contract.secType() +
        		", Currency: " + contract.currency() +
        		", Position: " + pos + 
        		", Avg cost: " + avgCost);
    }
	
	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		logger.log("Local symbol: " + contractDetails.contract().localSymbol() + 
				   " contract id: " + contractDetails.contract().conid());
		//if (contractDetails.contract().symbol() == inst.symbol() && 
		//	contractDetails.contract().secIdType() == inst.secIdType()) {
		this.underConID = contractDetails.contract().conid();	
		//}
	}
	
	@Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange,
            int underlyingConId, String tradingClass, String multiplier,
            Set<String> expirations, Set<Double> strikes) {
        logger.log("Security Definition Optional Parameter. Request: " + reqId +
        		   ", Trading Class: " + tradingClass +
        		   ", Multiplier: " + multiplier +
        		   " \n");
        logger.log("Expirations: " + expirations.toString());
        logger.log("Strikes: " + strikes.toString());
    }
}
