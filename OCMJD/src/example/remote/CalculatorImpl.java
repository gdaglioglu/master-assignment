package example.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

	// TODO serialVersionUID
	private static final long serialVersionUID = 5232401150631985592L;

	public CalculatorImpl() throws RemoteException {
		super();
	}

	public long add(long a, long b) throws RemoteException {
		System.out.println("Doing addition");
		return a + b;
	}

	public long sub(long a, long b) throws RemoteException {
		System.out.println("Doing subtraction");
		return a - b;
	}

	public long mul(long a, long b) throws RemoteException {
		System.out.println("Doing multiplication");
		return a * b;
	}

	public long div(long a, long b) throws RemoteException {
		System.out.println("Doing division");
		return a / b;
	}
}