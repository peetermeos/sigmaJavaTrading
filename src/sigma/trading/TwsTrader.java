package sigma.trading;

import java.io.IOException;
import java.util.Set;
import java.util.Vector;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderType;
import com.ib.client.TagValue;

import sigma.utils.Logger;
import sigma.utils.LogLevel;

public class TwsTrader implements EWrapper {
	
	// TWS internals
	private EJavaSignal m_signal = new EJavaSignal();
    private EReader m_reader;
    private EClientSocket tws;
    
    private int nextOrderID = 0;
    private String genericTickList = null;
    
    // Contract structure
    private Contract inst;
   
    private Order longStop;
    private Order shortStop;
    private Order longTrail;
    private Order shortTrail;
    
    private double spotPrice = 0;
    private double delta = 0;
    private double trailAmt = 0;
    private double q = 0;
    
    // Threads
    private Logger logger;
    private Thread msgThread;
    
    class Task implements Runnable {
        public void run(){
          while (!Thread.currentThread().isInterrupted()){
            processMessages();    
          }
        }
    }
	
	public TwsTrader() {
		logger = new Logger(LogLevel.INFO);
		logger.log("Sigma News Trader init.");
		
			tws = new EClientSocket(this, m_signal);
	}
	
	public void twsConnect(String host, int port) {
	    tws.eConnect(host, port, 55);
	    
	    while (! tws.isConnected())
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				logger.error(e);
			}
	    logger.log("Connected");

	    m_reader = new EReader(tws, m_signal);
	    m_reader.start();
	    
	    Task myTask = new Task();
	    msgThread = new Thread(myTask, "T1");
	    msgThread.start();	
	}
	
	public void twsConnect() {
		twsConnect("127.0.0.1", 4001);
	}
	
	public void doTrading() {
		logger.log("Requesting market data");
		Vector<TagValue> mktDataOptions = new Vector<TagValue>();
		tws.reqMktData(1, inst, genericTickList, false, mktDataOptions);
		
		logger.log("Entering main trading loop");
		try {
			tws.reqCurrentTime();

			// Infinite loop until keypress
			while (System.in.available() == 0) {
				Thread.sleep(100);
				// Here check open orders and adjust them if needed
			}
			
		} catch (InterruptedException | IOException e) {
			logger.error(e);
		}
		logger.log("Main trading loop finished");
	}
	
	public void twsDisconnect() {
		logger.log("News trader exiting.");
					
		m_reader.interrupt();
		msgThread.interrupt();
		
		tws.eDisconnect();
	}
	
	private void processMessages() {
	    try{
	      m_reader.processMsgs();
	    } catch(Exception e){
	      error(e);
	    }
	}
	  
	public void createContracts() {
		logger.log("Creating contract structure");
		
		inst = new Contract();
		inst.exchange("NYMEX");
		inst.symbol("CL");
		inst.secType("FUT");
		inst.multiplier("1000");
		inst.lastTradeDateOrContractMonth("201710");
		
		// Requesting contract details
		logger.log("Requesting contract details");
		tws.reqContractDetails(nextOrderID, inst);
	}
	
	public void createOrders() {
		logger.log("Creating order structure");
		
		longStop = new Order();
		shortStop = new Order();
		longTrail = new Order();
		shortTrail = new Order();
		
		// Types
		longStop.orderType(OrderType.STP_LMT);
		shortStop.orderType(OrderType.STP_LMT);
		longTrail.orderType(OrderType.TRAIL_LIMIT);
		shortTrail.orderType(OrderType.TRAIL_LIMIT);
		
		// Actions
		longStop.action("BUY");
		shortStop.action("SELL");
		longTrail.action("SELL");
		shortTrail.action("BUY");
		
		// Quantities
		q = 1;
		longStop.totalQuantity(q);
		shortStop.totalQuantity(q);
		longTrail.totalQuantity(q);
		longTrail.totalQuantity(q);
		
		// Prices
		// First wait to have spot price
		while (spotPrice == 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
		
		delta = 0.05;
		trailAmt = 0.1;
		
		longStop.lmtPrice(spotPrice + delta);
		longStop.transmit(false);
		longStop.orderId(1001);
		
		shortStop.lmtPrice(spotPrice + delta);
		shortStop.transmit(false);
		shortStop.orderId(1002);
		
		longTrail.trailStopPrice(spotPrice);
		longTrail.auxPrice(trailAmt);
		longTrail.parentId(longStop.orderId());
		longTrail.transmit(false);
		
		shortTrail.trailStopPrice(spotPrice);
		shortTrail.auxPrice(trailAmt);
		shortTrail.parentId(shortStop.orderId());
		shortTrail.transmit(false);
		
		// OCO groupings and attached orders
		longStop.ocaGroup("NT");
		shortStop.ocaGroup("NT");
		
		logger.log("Placing orders");
		//tws.placeOrder(nextOrderID, inst, longStop);
		//tws.placeOrder(nextOrderID, inst, shortStop);
		//tws.placeOrder(nextOrderID, inst, longTrail);
		//tws.placeOrder(nextOrderID, inst, shortTrail;
	}
	
	public void adjustOrders(double spotPrice) {
		logger.log("Adjusting orders");
		longStop.lmtPrice(spotPrice + delta);
		shortStop.lmtPrice(spotPrice - delta);
		longTrail.trailStopPrice(spotPrice);
		shortTrail.trailStopPrice(spotPrice);
		
		logger.log("Placing orders");
		//tws.placeOrder(nextOrderID, inst, longStop);
		//tws.placeOrder(nextOrderID, inst, shortStop);
		//tws.placeOrder(nextOrderID, inst, longTrail);
		//tws.placeOrder(nextOrderID, inst, shortTrail;
	}
	
	public void cancelOrders() {
		// tws.cancelOrder(id);
	}
	
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		String tckType = null;
		
		switch(field) {
		case 1: 
			tckType = "bid";
			break;
		case 2:
			tckType = "ask";
			break;
		case 4:
			tckType = "last";
			spotPrice = price;
			break;
		default:
			tckType = null;
		}
		if (tckType != null) {
			logger.log("Price ticker " + tickerId + " field " + tckType + " price " + price);	
		}
			
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		logger.verbose("Size ticker " + tickerId + " field " + field + " size " + size);	
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		logger.verbose("Generic ticker " + tickerId + " type " + tickType + " value " + value);		
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
		logger.verbose("String ticker " + tickerId + " type " + tickType + " value " + value);		
	}

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints,
			double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact,
			double dividendsToLastTradeDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
		logger.log("Order " + orderId + " status " + status +
				" filled " + filled + " remaining " + remaining +
				" avgFillPrice " + avgFillPrice);
		
	}

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		logger.log("Order " + orderId + " contract" + contract.symbol() + 
				" order " + order.action() + order.orderType().toString() +
				" state " + orderState.toString());	
	}

	@Override
	public void openOrderEnd() {
		logger.log("Finishing opening order.");
		
	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
		logger.verbose("Update acct value :" + key + " value " + value + " " + currency);
	}

	@Override
	public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountTime(String timeStamp) {
		logger.log("Account timestamp: " + timeStamp);	
	}

	@Override
	public void accountDownloadEnd(String accountName) {
		logger.verbose("End of download for account: " + accountName);		
	}

	@Override
	public void nextValidId(int orderId) {
		logger.log("Updating order ID to:" + orderId);
		nextOrderID = orderId;
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		logger.verbose("Contract details req: " + reqId + " " + contractDetails.toString());
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		logger.verbose("Bond contract details req: " + reqId + " " + contractDetails.toString());		
	}

	@Override
	public void contractDetailsEnd(int reqId) {
		logger.verbose("End of contract details for req: " + reqId);		
	}

	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		logger.log("Exec details req: " + reqId + " contract " + 
				contract.toString() + " execution " + execution.toString());
	}

	@Override
	public void execDetailsEnd(int reqId) {
		logger.verbose("End of execution details for req: " + reqId);		
	}

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price,
			int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void managedAccounts(String accountsList) {
		logger.log("Managed accounts :" + accountsList);		
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume,
			int count, double WAP, boolean hasGaps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerParameters(String xml) {
		logger.verbose("Scanner parameters received.");	
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerDataEnd(int reqId) {
		logger.verbose("End of scanner data for req: " + reqId);
		
	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume,
			double wap, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void currentTime(long time) {
		logger.log("Current time is: " + time);
	}

	@Override
	public void fundamentalData(int reqId, String data) {
		logger.log("Fundamendal data for req: " + reqId + " : " + data);		
	}

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		logger.verbose("End of snapshot tick data for req: " + reqId);		
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commissionReport(CommissionReport commissionReport) {
		logger.verbose("Commission report " + commissionReport.toString());	
	}

	@Override
	public void position(String account, Contract contract, double pos, double avgCost) {
		logger.log("Position " + account + " : " + contract.toString() + 
				" pos " + pos + " avg cost" + avgCost);
		
	}

	@Override
	public void positionEnd() {
		logger.verbose("End of position report");
	}

	@Override
	public void accountSummary(int reqId, String account, String tag, String value, String currency) {
		logger.log("Summary " + tag + value + " " + currency);		
	}

	@Override
	public void accountSummaryEnd(int reqId) {
		logger.log("End of account summary for req: " + reqId);
		
	}

	@Override
	public void verifyMessageAPI(String apiData) {
		logger.verbose("Verify message API " + apiData);		
	}

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupList(int reqId, String groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Exception e) {
		logger.error(e);
	}

	@Override
	public void error(String str) {
		logger.error(str);		
	}

	@Override
	public void error(int id, int errorCode, String errorMsg) {
		logger.error(id + " " + errorCode + " " + errorMsg);
	}

	@Override
	public void connectionClosed() {
		logger.log("TWS connection closed");
		
	}

	@Override
	public void connectAck() {
		logger.verbose("Trying to connect");
	}

	@Override
	public void positionMulti(int reqId, String account, String modelCode, Contract contract, double pos,
			double avgCost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionMultiEnd(int reqId) {
		logger.verbose("End of multi position report for req: " + reqId);		
	}

	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value,
			String currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountUpdateMultiEnd(int reqId) {
		logger.verbose("End of multi account update for req: " + reqId);		
	}

	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId,
			String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

}
