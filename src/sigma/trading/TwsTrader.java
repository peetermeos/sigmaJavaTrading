package sigma.trading;

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
import com.ib.client.TagValue;

import sigma.utils.Logger;

public class TwsTrader implements EWrapper {
	
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
    
    private Logger logger;
    private Thread msgThread;
    
    class Task implements Runnable {
        public void run(){
          while (!Thread.currentThread().isInterrupted()){
            processMessages();    
          }
        }
    }
	
	public TwsTrader() throws InterruptedException {
		logger = new Logger();
		logger.log("Sigma News Trader init.");
		
		tws = new EClientSocket(this, m_signal);
	}
	
	public void twsConnect() {
	    tws.eConnect("127.0.0.1", 4001, 0);

	    m_reader = new EReader(tws, m_signal);
	    m_reader.start();
	    
	    Task myTask = new Task();
	    msgThread = new Thread(myTask, "T1");
	    msgThread.start();
	}
	
	public void doTrading() {
		logger.log("Requesting market data");
		Vector<TagValue> mktDataOptions = new Vector<TagValue>();
		tws.reqMktData(1, inst, genericTickList, false, mktDataOptions);
		
		logger.log("Entering main trading loop");
		try {
			Thread.sleep(1000);
			tws.reqCurrentTime();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
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
	}
	
	public void createOrders() {
		logger.log("Creating order structure");
		
		longStop = new Order();
		shortStop = new Order();
		longTrail = new Order();
		shortTrail = new Order();
	}
	
	public void adjustOrders(double spotPrice) {
		
	}
	
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		logger.log("Ticker " + tickerId + " field " + field + " price " + price);		
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		logger.log("Ticker " + tickerId + " field " + field + " size " + size);	
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAccountTime(String timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountDownloadEnd(String accountName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextValidId(int orderId) {
		logger.log("Updating order ID to:" + orderId);
		nextOrderID = orderId;
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contractDetailsEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execDetailsEnd(int reqId) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scannerDataEnd(int reqId) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commissionReport(CommissionReport commissionReport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void position(String account, Contract contract, double pos, double avgCost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionEnd() {
		logger.log("End of position report");
	}

	@Override
	public void accountSummary(int reqId, String account, String tag, String value, String currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountSummaryEnd(int reqId) {
		logger.log("End of account summary");
		
	}

	@Override
	public void verifyMessageAPI(String apiData) {
		// TODO Auto-generated method stub
		
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
		logger.log("Trying to connect");
	}

	@Override
	public void positionMulti(int reqId, String account, String modelCode, Contract contract, double pos,
			double avgCost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionMultiEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value,
			String currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountUpdateMultiEnd(int reqId) {
		// TODO Auto-generated method stub
		
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
