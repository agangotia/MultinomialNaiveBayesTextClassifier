package com.anupam.naivebayes;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.anupam.Constants;

/**
 * @author Anupam Gangotia
 * Profile::http://en.gravatar.com/gangotia
 * github::https://github.com/agangotia
 */
/**
 * This is the Matrix datatype.,
 */
public class MatrixData {
	int coloumns;// number of coloumns in matrix
	ArrayList<String> Headers;// Values in headers of matrix
	ArrayList<int[]> rows;// rows in matrix
	int Numrows;// number of rows

	// Default constructor
	public MatrixData() {
		rows = new ArrayList<int[]>();
		Headers = new ArrayList<String>();
	}

	// Default constructor
	public MatrixData(ArrayList<String> Headers, ArrayList<int[]> rows,
			int coloumns, int Numrows) {
		this.Headers = Headers;
		this.rows = rows;
		this.coloumns = coloumns;
		this.Numrows = Numrows;
	}

	/**
	 * MatrixData Constructor
	 * 
	 * @param input
	 *            : ArrayList of String[] example:
	 *            Attribute1,Attribute2,Attribute3,.....AttributeN,Class
	 *            1,0,0,....4,1 0,2,2,....4,1 1,0,0,....4,0 For Attributes 1...N
	 *            1,2,3... : specifies number of times that words has been
	 *            observed in that training instance For Class 1: Instance
	 *            classifed as true 0: Instance classified as false
	 */
	public MatrixData(ArrayList<String[]> input) throws Exception{
		if(Constants.isDebug){
			System.out.println("Reading Data into MAtrix Datatype");
		}
		int rowTuples = 0;
		rows = new ArrayList<int[]>();
		Headers = new ArrayList<String>();
		if (input != null) {
			for (String[] arryAtributes : input) {
				if (arryAtributes != null && arryAtributes.length > 0) {
					if (rowTuples == 0) {// First Row HEader
						for (int i = 0; i < arryAtributes.length; i++) {
							Headers.add(arryAtributes[i]);
						}
						rowTuples++;
						this.coloumns = Headers.size();
						if(Constants.isDebug){
							System.out.println("Header Successfully REad");
						}
					} else {
						if(arryAtributes!=null){
							if(Constants.isDebug){
								//System.out.println("-Instance "+rowTuples+"-"+arryAtributes);
							}
							
							int[] valuesInt = new int[arryAtributes.length];
							for (int i = 0; i < this.coloumns; i++) {
								if (arryAtributes[i]!=null) {
									valuesInt[i] = Integer.parseInt(arryAtributes[i]);
								} else {
									System.out.println("Not a valid integer"+arryAtributes[i]);
									System.out.println("returning");
								}

							}
							rows.add(valuesInt);
							rowTuples++;
						}
						
					}
				} else {
					System.out.println("Error::Undefined Data");
					System.out.println("returning");
				}

			}
			
			
			
			this.Numrows = rowTuples - 1;

		} else {
			System.out
					.println("Error::MatrixData is supplied with a null value");
			System.out.println("returning");
		}

	}

	public ArrayList<String> getHeaders() {
		return Headers;
	}

	public void setHeaders(ArrayList<String> headers) {
		Headers = headers;
	}

	public ArrayList<int[]> getRows() {
		return rows;
	}

	public void setRows(ArrayList<int[]> rows) {
		this.rows = rows;
	}

	// This function prints the matrix read from the file
	// usefull in debugging
	public void printMatrix() {
		// print header
		for (String temp : Headers) {
			System.out.print("\t" + temp);
		}
		for (int[] temp : rows) {
			System.out.println("");
			for (int i = 0; i < coloumns; i++) {
				System.out.print("\t" + temp[i]);
			}
		}
		System.out.println("");
	}

	// FIlls the given array with values for which index is matched to the given
	// index
	public void fillArray(int[] arrayToFill, int indexToFetch) {
		int arrIndex = 0;
		for (int[] temp : rows) {
			// System.out.print("\n"+temp[indexToFetch]);
			arrayToFill[arrIndex++] = temp[indexToFetch];
		}
	}

	// Reads the file and prepares the matrix
	public void prepareMatrix(String FileNameToRead,
			int PercentageOfDataToLEarnFrom) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(FileNameToRead));
			int NumberOfColoumns = 0;
			// Reading Header
			{
				String line = br.readLine();
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreElements()) {
					Headers.add((String) st.nextElement());
				}

			}

			NumberOfColoumns = Headers.size();
			coloumns = NumberOfColoumns;
			{// lets read coloumns
				String line = br.readLine();
				while (line != null) {
					// System.out.print(line);
					StringTokenizer st = new StringTokenizer(line, ",");
					// System.out.println("---- Split by space ------");
					int[] tempCol = new int[NumberOfColoumns];
					int tempIndex = 0;
					while (st.hasMoreElements()) {
						tempCol[tempIndex++] = Integer.parseInt((String) st
								.nextElement());
						// System.out.println(tempCol[tempIndex-1]);
					}
					rows.add(tempCol);
					line = br.readLine();
				}
			}

			Numrows = rows.size();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * Function :: splitMatrix, This function splits the matrix based on the
	 * given attribute name, and its value., rest other rows are removed from
	 * the row.
	 */
	public MatrixData splitMatrix(String attributeName, int value) {// this
																	// fucntion
																	// needs
																	// some
																	// modifications
		MatrixData matrixReturn = new MatrixData();
		ArrayList<int[]> rowsMatrixReturn = new ArrayList<int[]>();
		ArrayList<String> HeadersMatrixReturn = new ArrayList<String>();
		int attributeIndex = 0;

		try {
			// First set into headers all headers except the Selected Attribute
			for (String tempHeadValue : Headers) {
				if (!tempHeadValue.equals(attributeName))
					HeadersMatrixReturn.add(tempHeadValue);
			}
			matrixReturn.coloumns = HeadersMatrixReturn.size();
			matrixReturn.setHeaders(HeadersMatrixReturn);

			for (attributeIndex = 0; attributeIndex < Headers.size(); attributeIndex++) {
				if (attributeName.equals(Headers.get(attributeIndex))) {
					break;
				}
			}
			// System.out.println("Attribute found at:"+attributeIndex);

			for (int[] temp : rows) {
				if (temp[attributeIndex] == value) {
					int[] tempRow = new int[HeadersMatrixReturn.size()];
					int indexTempRow = 0;
					for (int i = 0; i < coloumns; i++) {
						if (i == attributeIndex) {

						} else {
							tempRow[indexTempRow] = temp[i];
							indexTempRow++;
						}
					}

					rowsMatrixReturn.add(tempRow);
				}

			}
			matrixReturn.setRows(rowsMatrixReturn);
			matrixReturn.Numrows = rowsMatrixReturn.size();
			return matrixReturn;

		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * Function getNumClassifiedAs()
	 * 
	 * @param ClassValue
	 *            This is the class value 0-false, 1- true
	 * @return the count of attributes that satisfy the critereon
	 */
	public int getNumClassifiedAs(int ClassValue) {
		int count = 0;
		for (int[] temp : rows) {

			if (temp[coloumns - 1] == ClassValue) {
				for (int i = 0; i < coloumns; i++) {
					if (temp[i] > 0)
						count += count;
				}

			}
		}
		return count;
	}

	/**
	 * Function getNumClassifiedAs()
	 * 
	 * @param ClassValue
	 *            This is the class value 0-false, 1- true
	 * @return the count of attributes that satisfy the critereon
	 */
	public void fillMapClassifiedAs(HashMap<String, Integer> numAttrClassified,
			int ClassValue) {

		for (int i = 0; i < coloumns - 1; i++) {
			int count = 0;
			for (int[] temp : rows) {
				if (temp[i] > 0 && temp[coloumns - 1] == ClassValue)
					count += temp[i];
			}
			numAttrClassified.put(Headers.get(i), count);
		}

	}
	
	
	/**
	 * Function getTrainingInstancesClassifiedAs()
	 * 
	 * @param ClassValue
	 *            This is the class value 0-false, 1- true
	 * @return the count of Instances that satisfy the critereon
	 */
	public int getTrainingInstancesClassifiedAs(int ClassValue) {
		int count = 0;
		for (int[] temp : rows) {

			if (temp[coloumns - 1] == ClassValue) {
				count ++;
			}
		}
		return count;
	}

}
