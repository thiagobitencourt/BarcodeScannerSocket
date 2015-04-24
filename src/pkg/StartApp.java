/****************************************************************************
**
** WWW.CELTAB.ORG.BR
** WIKI.CELTAB.ORG.BR
**
** Copyright (C) 2015
**                     Thiago R. M. Bitencourt <thiago.mbitencourt@gmail.com>
**
** This file is part of the ItaipuBarcodeScanner project
**
** This program is free software; you can redistribute it and/or
** modify it under the terms of the GNU General Public License
** as published by the Free Software Foundation; version 2
** of the License.
**
** This program is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
** GNU General Public License for more details.
**
** You should have received a copy of the GNU General Public License
** along with this program; if not, write to the Free Software
** Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
**
****************************************************************************/
package pkg;

import java.io.IOException;
import java.util.Scanner;

/*
 * Thiago R. M. Bitencourt
 * thiago.mbitencourt@gmail.com
 * 
 * Esta classe é responsável por executar o aplicativo BarcodeScanner.
 * Executa, na verdade, um script em PowerShell que esta sim irá executar o aplicativo.
 * 
 * A classe também captura as informações recebidas do script e retorna para que usam usadas para depuração e/ou apresentação para o usuário
 */
public class StartApp {

	@SuppressWarnings("resource")
	public String execCmd() throws IOException { 
		
		/*Comando a ser executado
			Executa em Windows PowerShell
			-ExecutionPolicy Bypass: permite que um script "não oficial" seja executado no sistema
			runApp.ps1, script PowerShell que executa o aplicativo
		*/ 
		String cmd = "PowerShell.exe -ExecutionPolicy Bypass -File runApp.ps1";
		Process proc = Runtime.getRuntime().exec(cmd); 
		
		// Cria um scanner para capturar os retornos da execução do comando
		Scanner s = new Scanner(proc.getInputStream()).useDelimiter("\\A"); 
		
		String res = "";
		while(s.hasNext()){
			//Concatena todas as informações recebidas em uma váriavel que será retornada.
			res += s.next();
		}
		return res;
	}
}
