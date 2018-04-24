package com.cll.entity;

public class Fraction {

	private int numerator; // ����
	private int denominator; 
	public static final Fraction ZERO=new Fraction(0, 1);// ��ĸ

	public Fraction(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public double getValue() {
		return (double) numerator / (double) denominator;
	}

	public int getNumerator() {
		return numerator;
	}

	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}

	public int getDenominator() {
		return denominator;
	}

	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}

	public Fraction add(Fraction fraction) {
		int num = fraction.getNumerator();
		int de = fraction.getDenominator();
		return add(num, de);
	}

	public Fraction add(int num, int de) {
		if (denominator == de) {
			return new Fraction(num + numerator, de);
		}else if (num==0) {
			return this;
		} else {
			num = numerator * de + num * denominator;
			de = de * denominator;
			int maxCommonDivisor = maxCommonDivisor(num, de);
			num = num / maxCommonDivisor;
			de = de / maxCommonDivisor;
			return new Fraction(num, de);
		}

	}
	
	public Fraction div(int num, int de){
		
		int n=numerator*de;
		int d=denominator*num;
		if (n==0) {
			return ZERO;
		}
		int maxCommonDivisor = maxCommonDivisor(n, d);
		n = n / maxCommonDivisor;
		d = d / maxCommonDivisor;
		return new Fraction(n, d);
	}

	public Fraction div(Fraction fraction){
		int num = fraction.getNumerator();
		int de = fraction.getDenominator();
		return div(num, de);
	}
	
	@Override
	public String toString() {

		return numerator + "/" + denominator;
	}

	private int maxCommonDivisor(int m, int n) {
		if (m < n) {// ��֤m>n,��m<n,��������ݽ���
			int temp = m;
			m = n;
			n = temp;
		}
		if (m % n == 0) {// ������Ϊ0,�������Լ��
			return n;
		} else { // ����,���еݹ�,��n����m,����������n
			return maxCommonDivisor(n, m % n);
		}
	}
}
