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

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JTextArea;

/*
 * Thiago R. M. Bitencourt
 * thiago.mbitencourt@gmail.com
 * 
 * A classe Conection é responsável por criar um server socket que irá aguardar conexão do aplicativo BarcodeScanner
 * 
 * O aplicativo BarcodeScanner envia as informações de decodificação de um código de barras no cabeçalho do handShake.
 * As informações são recebidas na linha referente aos protocolos (WebSocket-Protocol). 
 * Para responder ao hasdshake é criado um HASH de resposta baseado nas informações recebidas no cabeçalho do handshake.
 * 
 */
public class Conection extends Thread {

	private ServerSocket server = null;
	private Scanner entrada = null;	
	private DataOutputStream saida = null;
	private Socket cliente = null;
	
	final String newLine = "\r\n";
	JTextArea textArea;
	JLabel label;
	final int porta = 6788;

	// O Contrutor recebe o texteArea e o label que a classe irá utilizar para apresentar alguns resultados.
	public Conection(JTextArea textArea, JLabel label){
		this.textArea = textArea;
		this.label = label;
		textArea.append("Connection Started!" + newLine);
	}

	// método chamado quando a Thread é iniciada utilizando o método start();
	public void run() {

		try{
			int count = 0;
			String msg = "";

			// Variaveis utilizadas para armazenar as informações de type e code, provindas da leitura de um código de barras.
			String type = "";
			String code = "";

			String key = "";
			String response = "";

			System.out.println("Localhost IP: " + InetAddress.getByName("localhost").toString());//getHostAddress());

			server = new ServerSocket(porta);

			textArea.append(newLine + "Aguardando conexão em: " + InetAddress.getByName("localhost").getHostAddress() + newLine);
			System.out.println("Aguardando Conexão");
			cliente = server.accept();

			textArea.append("Cliente Conectado..." + newLine);

			System.out.println("Cliente Conectado...");
			entrada = new Scanner(cliente.getInputStream());
			saida = new DataOutputStream(cliente.getOutputStream());

			// Loop de 14 "voltas". O número 14 foi escolhido porque após a décima-quarta linha, as informação não são mais relevantes (para este caso específicamente).
			while(count < 14){
				//Verifica se existe uma nova linha a ser lida. Pode acontecer de haver uma linha para ser lida, porém, esta linha é vazia.
				// Isso acontece na última linha de solicitação de handshake
				if(entrada.hasNextLine())
				{
					// Faz a leitura da nova linha
					msg = entrada.nextLine();
					System.out.println(msg);
					
					if(msg.equals("")){
						//Se a linha recebida for vazia, caracteriza a ultima linha da mensagem. Portanto, o loop pode ser finalizado
						break;
					}

					/* 
						As informações de tipo da codificação e o código em si são recebidos já na solicitação de handshake. 
						Essas informações são enviadas na parte do cabeçalho que define os protocolos a serem utilizados na conexão. 
						Portanto, quando a linha que esta sendo lida contém a string "WebSocket-Protocol:" significa que é nesta linha que deve ser feita a extração das informações esperadas.

						As informações de tipo da codificação e o código própriamente dito são armazenados nas variavies type e code, respectivamente.
					 */
					if(msg.contains("WebSocket-Protocol:")){
						String[] arr = msg.replace(",", "#").replace(" ", "#").split("[#]+");

						String tmp = msg.replace(",", "#").replace(" ", "#");
						textArea.append(newLine + tmp);

						type = arr[1];
						code = arr[2];

						System.out.println(type + "\n" + code);
						textArea.append(newLine + "Type: " + type + newLine + "Code: " + code  + newLine);
						label.setText("Code: " + code);
					}

					/*
						Para que o handshake seja feito é necessário enviar uma resposta com HASH baseado em uma chave enviada na solicitação.
						O código a baixo é responsável por extrair esta chave do cabeçalho da requisição de handshake e criar a chave de resposta.
						
						A chave a ser acoplada na resposta é armazenada na variavel key.
					 */
					if(msg.contains("WebSocket-Key:")){
						// Extrai a chave enviada pelo cliente
						key = msg.split(" ")[1];
						// Adiciona um skip a esta chave
						key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

						// Cria o HASH da chave recebida.
						MessageDigest crypt = MessageDigest.getInstance("SHA-1");
						crypt.reset();
						key = new String(Base64.encode(crypt.digest(key.getBytes("UTF-8"))));
					}
				}else {
					// Se não houver mais linhas para serem lidas, sai do loop.
					break;
				}
				count ++;
			}
			
			// Monta o objeto de resposta para estabelecer o handShake
			response = "HTTP/1.1 101 Switching Protocols" + newLine
					+ "Upgrade: websocket" + newLine
					+ "Connection: Upgrade" + newLine
					+ "Sec-WebSocket-Accept: " + key + newLine
					+ "Sec-WebSocket-Protocol: " + type + newLine
					+ "Sec-WebSocket-Version: 13" + newLine + newLine
					;

			System.out.println(newLine + "Response: " + newLine + response + newLine);
			textArea.append(newLine + newLine + "Response: " + newLine + response + newLine);
			
			//Envia a resposta para estabelecer o handshake
			saida.writeUTF(response);

			textArea.append("Fim da Conexão!" + newLine);
			
		} catch (Exception e2) {
			textArea.append(newLine + e2.getMessage());
			System.out.println("ERROR: " + e2);
		}
		// A conexão deve ser fechada mesmo se algum erro ocorrer, para possibilitar que um novo socket seja iniciado na mesma porta.
		finally{
			try {
				
				/* Após estabelecer o handshake a conexão é encerrada.
				   Como as informações esperadas são recebidas já na solicitação de handshake, não é necessário manter a conexão aberta.
				   É enviada a resposta ao handshake apenas para que o cliente não apresente um erro para o usuário. O que poderia ser confuso. 
				 */
				entrada.close();
				saida.close();
				cliente.close();
				server.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
