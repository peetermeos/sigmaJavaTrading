package sigma.trading;

import java.util.Set;

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

import sigma.utils.LogLevel;
import sigma.utils.Logger;

/**
 * 
 * @author Peeter Meos
 * @version 0.5
 *
 */
public class TwsConnector implements EWrapper {
	// TWS internals
	protected EJavaSignal m_signal = new EJavaSignal();
	protected EReader m_reader;
	protected EClientSocket tws;
    
	protected int nextOrderID = 0;
    protected String myName;
    
    // Threads
    protected Logger logger;
    protected Thread msgThread;
    
    /**
     * 
     * @author Peeter Meos
     *
     */
    protected class Task implements Runnable {
        public void run(){
          while (!Thread.currentThread().isInterrupted()){
            processMessages();    
          }
        }
    }
    
    /**
     * Constructor for TwsConnector object. 
     * Simply creates the instance and starts
     * the logger.
     */
	public TwsConnector() {
		myName = "TWS Connector";
		
		logger = new Logger(LogLevel.INFO);
		logger.log(myName + " init.");
		
			tws = new EClientSocket(this, m_signal);
	}
	
	/**
	 * 
	 * @param m_name
	 */
	public TwsConnector(String m_name) {
		this.myName = m_name;
		logger = new Logger(LogLevel.INFO);
		logger.log(myName + " init.");
		
		tws = new EClientSocket(this, m_signal);
	}
	
	/**
	 * Connection method for TwsConnector
	 * @param host host name for TWS or IB Gateway
	 * @param port port for TWS or IB Gateway
	 */
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
	
	/**
	 * Connection method for TwsTrader.
	 * Connects to localhost port 4001
	 */
	public void twsConnect() {
		twsConnect("127.0.0.1", 4001);
	}
	
	/**
	 * Disconnects from TWS API. First stops the reader, 
	 * then stops message processor and finally closes
	 * the socket.
	 */
	public void twsDisconnect() {
		logger.log(myName + " exiting.");

		logger.log("Closing socket");
		tws.eDisconnect();
		
		logger.log("Stopping message processessor");
		msgThread.interrupt();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		msgThread = null;
		
		logger.log("Stopping reader");
		m_reader.interrupt();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		m_reader = null;
	}
	
	/**
	 * Starts message processor thread.
	 */
	protected void processMessages() {
	    try{
	      m_reader.processMsgs();
	    } catch(Exception e){
	      error(e);
	    }
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
		logger.log("Tick option computation");		
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
		logger.verbose("Tick EFP");		
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
		logger.verbose("Update portfolio");			
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
		logger.verbose("Update market depth");		
	}

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price,
			int size) {
		logger.verbose("Update market depth L2");		
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		logger.verbose("Update news bulletin");		
	}

	@Override
	public void managedAccounts(String accountsList) {
		logger.log("Managed accounts :" + accountsList);		
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		logger.verbose("Receive FA");
	}

	@Override
	public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume,
			int count, double WAP, boolean hasGaps) {
		logger.verbose("Historical data received.");		
	}

	@Override
	public void scannerParameters(String xml) {
		logger.verbose("Scanner parameters received.");	
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		logger.verbose("Scanner data");		
	}

	@Override
	public void scannerDataEnd(int reqId) {
		logger.verbose("End of scanner data for req: " + reqId);		
	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume,
			double wap, int count) {
		logger.verbose("Realtime bar received.");		
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
		logger.log("Delta neutral validation");		
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		logger.verbose("End of snapshot tick data for req: " + reqId);		
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		logger.verbose("Market data type received");		
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
		logger.verbose("Verify completed msg received");
	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
		logger.verbose("Verify and auth message API");	
	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		logger.verbose("Verify and auth completed");		
	}

	@Override
	public void displayGroupList(int reqId, String groups) {
		logger.verbose("Display group list");		
	}

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		logger.verbose("Display group updated");	
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
		logger.verbose("Position multi");
		
	}

	@Override
	public void positionMultiEnd(int reqId) {
		logger.verbose("End of multi position report for req: " + reqId);		
	}

	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value,
			String currency) {
		logger.verbose("Account update multi");		
	}

	@Override
	public void accountUpdateMultiEnd(int reqId) {
		logger.verbose("End of multi account update for req: " + reqId);		
	}

	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId,
			String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
		logger.log("Security defition optional parameter");		
	}

	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) {
		logger.verbose("Securoty defition optional parameter end.");		
	}

}
