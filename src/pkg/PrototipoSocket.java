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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * Thiago R. M. Bitencourt
 * thiago.mbitencourt@gmail.com
 * 
 * Protótipo com uma interface gráfica simples para validar o uso de sockets para comunicação com o aplicativo BarcodeScanner.
 * A interface gráfica conta apenas com um botão para abrir o socket e executar um comando que irá abrir o aplicativo. 
 * Além disso, conta com um campo de texto no qual será exibido informações de log de acordo com a execução da aplicação. 
 * Estes logs servem apenas para fins de depuração.
 * 
 * Sempre que o botão for clicado o socket será aberto e o aplicativo iniciado. Caso o socket ja esteja aberto, será apresentado uma mensagem
 * de erro no campo de texto.
 */
public class PrototipoSocket {

	public static void main(String[] args) {
		final JFrame frame = new JFrame("Socket Sample");

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JButton buttonApp = new JButton();
		buttonApp.setText("Launch App");
		
		panel.add(buttonApp);
		
		final JTextArea textArea = new JTextArea(20, 50);
		textArea.setFont(new Font("Serif", Font.ITALIC, 16));
		textArea.setLineWrap(true);
			
		JScrollPane jScrollPane1 = new JScrollPane(textArea);
		jScrollPane1.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setPreferredSize(new Dimension(300, 250));

		frame.getContentPane().add(panel);
		final JLabel label = new JLabel("Não iniciado...");
		panel.add(label);
		panel.add(jScrollPane1);
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// Evento para o botão
		buttonApp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	
            	// A classe Connection cria um socket, inicia e aguarda a conexão de um cliente 
            	label.setText("Aguardando...");
            	Conection conn = new Conection(textArea, label);
            	conn.start();
            	
            	// A classe StartApp é responsável pela execução do comando PowerShell que executa o aplicativo BarcodeScanner
            	textArea.append("\r\nIniciando Aplicação...\n");
            	StartApp app = new StartApp();
            	try {
            		//Adiciona o retorno do comando ao campo de texto, para fins de depuração
            		textArea.append("\r\n" + app.execCmd() + "\n");
            		
            		// Tras a aplicação para o primeiro plano novamente
            		frame.toFront();
            		frame.repaint();
            		
				} catch (IOException e1) {
					// Se houver uma exeção, mostra a mensagem de erro nos logs
					textArea.append("\r\nError: " + e1.getMessage() + "\n");
				}
            }
        });
	}
}