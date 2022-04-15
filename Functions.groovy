

// -- FUNCTIONS --

class Functions{

    def context
    def testRunner
    def log
    
	// Find the column number for given column name
	def getColumn(name, ws, totalColumns){
		def row = ws.getRow(0)
		def colmnNumber
		for (def j=1; j<totalColumns; j++) {
			def cell = row.getCell(j)
			//log.info cell.toString()
			if (cell.toString() == name){
			colmnNumber = j
			break;
			}
		}
		return colmnNumber
	}

	//Return the value of a cell, rather than the formula and no matter what type
	def getCellValue(cell){
		def value
		log.info cell.getCellType()
		switch (cell.getCellType()){
			case "BOOLEAN":
            	value = cell.getBooleanCellValue();
            	break;
			case "FORMULA":
				    switch (cell.getCachedFormulaResultType()) {
        				case "BOOLEAN":
            				value = cell.getBooleanCellValue();
            				break;
        				case "NUMERIC":
           				 	value = cell.getNumericCellValue();
            				break;
        				case "STRING":
            				value = cell.getRichStringCellValue();
            				break;
					}
				break;
			case "STRING":
				value = cell.getRichStringCellValue();
				break;
			case "NUMERIC":
					value = cell.getNumericCellValue();
				break;
			default:
				value = cell.toString()
				break;
		}

		return value
	}

	//create Error Log Text from given arguments
	def createErrorLogText(message, expected, result){
		def errorText = []
		errorText << message + "\n"
		errorText << "Expected: " + expected + "\n"
		errorText << "Result: " + result + "\n"
		errorText << "----------------------------------------\n\n"
		
		return errorText
	}


	//Initiate folders
	def checkAndCreateFolder(name, path, date){
		File directory = new File(path + "\\" + name + "\\" + date);
		if (! directory.exists()){
		directory.mkdirs();
		}
	}

	//Save xls File
	def saveFile(name, path, wb){
		try{
			FileOutputStream fileOut = new FileOutputStream(path + "//" + name + ".xls");
			wb.write(fileOut);
			fileOut.close();
			log.info name + ".xls updated and saved"
		}
		catch(FileNotFoundException e){
			//when the file is open, save in new file	!!If the second one is also open = infinite loop!!
			log.error " xls FILE OPEN - Saving in new file..."
			saveFile(name + "_NEW", path, wb);
		}
	}

	//Write all lines of the error list to the error-log-file
	def writeListAsLinesToLog(list, i, path, date){
		def errorLogFile = new File(path + "\\Fails\\" + date + "\\row " + (i+1) +".txt");
		for (line in list){
			log.info line.toString()
			errorLogFile.append (line.toString());
		}
	}

	//check the difference between 2 values: <=0.05 is acceptable
	def checkValues(val1,val2){
		float dec1 = Float.parseFloat(val1);
		float dec2 = Float.parseFloat(val2);
		//make the value absolute and truncate at 2 decimals
		def difference = Math.abs(dec1 - dec2).trunc(2)
		if (difference > 0.05){
			return false
		}else{
			return true
		}
	}
	
	//TestFunction
	def printCall(){
		log.info "Function.groovy is loaded"
	}
}
