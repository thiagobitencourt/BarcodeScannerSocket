# BarcodeScannerSocket_Protótipo
Protótipo JAVA que implementa um socket e aguarda a conexão de um cliente que utiliza webSocket. 
O objetivo deste protótipo é apenas testar e validar o aplicativo ItaipuBarcodeScanner. Disponível em [ItaipuBarcodeScanner](https://github.com/CELTAB/ItaipuBarcodeScanner/tree/onlyCam)

O aplicativo BarcodeScanner utiliza a camera de um dispositivo móvel para capturar uma imagem de um código de barras e decodificá-lo.
O código de barras decodificado é enviado para uma aplicação JAVA através de um socket.

Portanto, este protótipo cria um socket e espera a conexão por parte do aplicativo BarcodeScanner. A interface gráfica deste protótipo conta apenas com um botão, que é responsável por criar e iniciar o socket e, posteriormente, iniciar o aplicativo.
Além do botão, o protótipo também conta com um campo de texto para informações de log para fins de depuração.

Dentro do diretório [release](release/) se encontram o arquivo .jar com a ultima versão do protótipo e um script PowerShell para executar o aplicativo BarcodeScanner em Windows 8.1. Ambos os arquivos devem estar no mesmo diretório para que o protótipo funcione corretamente.

Para entender um pouco mais sobre o projeto ItaipuBarcodeScanner acesse a página wiki do projeto, [NESTE LINK!](http://wiki.celtab.org.br/index.php/Itaipu_BarcodeScanner)

