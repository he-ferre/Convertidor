import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class app_1_0{
    public static void main(String [] args){
        new Ventana1();


    }  
}

//Constructor de ventanas
class Ventana1 extends JFrame{
    
    private JTextField campoTexto;

    public Ventana1(){
        setTitle("Conversor de sistemas de representacion");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255,255,200));
        setResizable(false);


        //Area para ingresar el texto / numero
        ingresarTexto();

        Boton miBoton = new Boton("Continuar", 185,100,200,40);
        add(miBoton); 

        //Accion al hacer click en el boton
        miBoton.addActionListener(e -> {
            String numeroIngresado = campoTexto.getText();
            new Ventana2(numeroIngresado, this);
            this.setVisible(false);
        });


        setVisible(true);
    }


    private void ingresarTexto(){

        JLabel etiqueta = new JLabel("Ingrese el numero: ");
        etiqueta.setBounds(220,25,200,40);
        add(etiqueta);

        campoTexto = new JTextField(15);
        campoTexto.setBounds(185,60,200,30);
        add(campoTexto);
    }
}

//Constructor botones
class Boton extends JButton {


    public Boton(String texto, int x, int y, int ancho, int alto) {
        super(texto); // Establece el texto en el constructor de JButton
        setBounds(x, y, ancho, alto);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(new Color(65, 105, 224));
        setForeground(Color.white);
        setFont(new Font("Arial", Font.BOLD, 14));

        // Acción al pasar el mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(65, 165, 224));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(65, 105, 224));
            }
        });
    }
}

// Constructor de la ventana 2
class Ventana2 extends JFrame{

    private String numero;
    private Ventana1 ventana1;

    public Ventana2(String numero, Ventana1 ventana1){

        this.numero = numero;
        this.ventana1 = ventana1;

        setTitle("Conversor");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255,255,200));
        setResizable(false);

        

        JLabel etiqueta = new JLabel("Seleccione sistema original y sistema destino :");
        etiqueta.setBounds(160,20,300,80);
        add(etiqueta);

        // ComboBox origen
        // String [] precision = {"32 bits (float)", "64 bits (double)" };
        // JComboBox<String> comboPrecision = new JComboBox(precision);
        // comboPrecision.setBounds(470,80,130,30);
        // add(comboPrecision);
        
        String[] sistemas = {"Decimal", "Binario", "Hexadecimal", "IEEE-754(32_Bits)","IEEE-754(64_Bits)" };
        JComboBox<String> comboOrigen = new JComboBox<>(sistemas);
        comboOrigen.setBounds(150,80,130,30);
        add(comboOrigen);

        // ComboBox destino
        JComboBox<String> comboDestino = new JComboBox<>(sistemas);
        comboDestino.setBounds(310,80,130,30);
        add(comboDestino);

        // Boton convertir
        Boton btnConvertir = new Boton("Convertir", 220,130,150,40);
        add(btnConvertir);

        // Boton volver
        Boton btnVolver = new Boton("Volver", 220,270,150,40);
        add(btnVolver);

        btnVolver.addActionListener(e ->{
            this.setVisible(false);
            ventana1.setVisible(true);
            });



        //Resultado
        JLabel resultadoLabel = new JLabel("Resultado: ");
        resultadoLabel.setBounds(40,200,600,40);
        resultadoLabel.setFont(new Font("Arial",Font.PLAIN,16));
        add(resultadoLabel);

        btnConvertir.addActionListener(e -> {
            String origen = (String) comboOrigen.getSelectedItem();
            String destino = (String) comboDestino.getSelectedItem();

            String resultado = "";

            if(origen.equals("Decimal") && destino.equals("Binario")){
                resultado = Conversion.decimalABinario(numero);
            }else if (origen.equals("Binario") && destino.equals("Decimal")) {
                resultado = Conversion.binarioADecimal(numero);
            }else if(origen.equals("Decimal") && destino.equals("Hexadecimal")){
                resultado = Conversion.decimalAHexadecimal(numero);
            }else if(origen.equals("Hexadecimal") && destino.equals("Decimal")){
                resultado = Conversion.hexadecimalADecimal(numero);
            }else if(origen.equals("Hexadecimal") && destino.equals("Binario")){
                resultado = Conversion.hexadecimalABinario(numero);
            }else if(origen.equals("Binario") && destino.equals("Hexadecimal")){
                resultado = Conversion.binarioAHexadecimal(numero);
            }else if(origen.equals("IEEE-754(32_Bits)") && destino.equals("Decimal") ){
                    resultado = Conversion.iEEE_754ADecimal(numero);
            }else if(origen.equals("IEEE-754(64_Bits)") && destino.equals("Decimal")){
                    resultado = Conversion.IEEE_754_64BitsADecimal(numero);
            }else if(origen.equals("Decimal") && destino.equals("IEEE-754(32_Bits)")){
                resultado = Conversion.decimalAIEEE_754(numero);
            }




            resultadoLabel.setText("Resultado: " + resultado);
        });

        setVisible(true);
    }
}
class Conversion{
    public static String decimalABinario(String numero){
        
        if(numero.contains(".") || numero.contains(",")){
            return decimalConFraccionABinario(numero);
        }else{
            try {
                int n = Integer.parseInt(numero);
                return Integer.toBinaryString(n);
            } catch (NumberFormatException e) {
                return "Numero invalido";
            }
        }

    }
    // Convercion de decimal con fraccion a binario
    private static String decimalConFraccionABinario(String numero){
        
        try{
            double valor = Double.parseDouble(numero);
            int parteEntera = (int) (valor);
            double parteFraccionaria = valor - parteEntera;

            String binarioEntero = Integer.toBinaryString(parteEntera);
            StringBuilder binarioFraccion = new StringBuilder();

            int precision = 10; // Cantidad maxima de bits fraccionarios
            while(parteFraccionaria > 0 && binarioFraccion.length() < precision){
                parteFraccionaria *= 2;
                if(parteFraccionaria >= 1){
                    binarioFraccion.append("1");
                    parteFraccionaria -= 1;
                }else{
                    binarioFraccion.append("0");
                }
            }
            return  binarioFraccion.length() > 0 
                ? binarioEntero + "." + binarioFraccion.toString()
                : binarioEntero;
        }catch(NumberFormatException e){
            return  "Numero invalido";
        }
    }
    public static String binarioADecimal(String numero){
        if(numero.contains(".") || numero.contains(",")){
            return binarioConFraccionADecimal(numero);
        }else{
            try{
                int n = Integer.parseInt(numero, 2);
                return String.valueOf(n);
            }catch(NullPointerException e){
                return "Numero invalido";
            }
        }
    }
    private static String binarioConFraccionADecimal(String numero){
        try {

            numero = numero.replace(",",".");

            String [] partes = numero.split("\\.");
            int parteEntera = Integer.parseInt(partes[0],2);

            double parteFraccionaria = 0;
            if(partes.length == 2){
                String fraccion = partes[1];
                for(int i = 0; i < fraccion.length(); i++){
                    if(fraccion.charAt(i) == '1'){
                        parteFraccionaria += 1 / Math.pow(2, i + 1);
                    }
                }
            }

            double resultado = parteEntera + parteFraccionaria;
            return String.valueOf(resultado);
        } catch (NumberFormatException e) {
            return "Numero invalido";
        }
    }
    public static String decimalAHexadecimal(String numero) {
        try {
            numero = numero.replace(",", ".");  // unificamos
    
            double numero1 = Double.parseDouble(numero);
            int parteEntera = (int) numero1;
            double parteFraccionaria = numero1 - parteEntera;
    
            String hexEntero = Integer.toHexString(parteEntera).toUpperCase();
    
            StringBuilder hexFraccion = new StringBuilder();
            int maxCifras = 10;  // cantidad de dígitos decimales que querés
            while (parteFraccionaria > 0 && hexFraccion.length() < maxCifras) {
                parteFraccionaria *= 16;
                int digito = (int) parteFraccionaria;
                hexFraccion.append(Integer.toHexString(digito).toUpperCase());
                parteFraccionaria -= digito;
            }
    
            if (hexFraccion.length() > 0) {
                return hexEntero + "." + hexFraccion.toString();
            } else {
                return hexEntero;
            }
    
        } catch (NumberFormatException e) {
            return "Número inválido";
        }
    }
    public static String hexadecimalADecimal(String numero) {
        try {
            numero = numero.toUpperCase().replace(",", ".");
            String[] partes = numero.split("\\.");
    
            // Parte entera
            int decimalEntero = Integer.parseInt(partes[0], 16);
    
            // Parte fraccionaria
            double decimalFraccion = 0;
            if (partes.length > 1) {
                String frac = partes[1];
                for (int i = 0; i < frac.length(); i++) {
                    int valor = Integer.parseInt(String.valueOf(frac.charAt(i)), 16);
                    decimalFraccion += valor / Math.pow(16, i + 1);
                }
            }
    
            double resultado = decimalEntero + decimalFraccion;
    
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "Número hexadecimal inválido";
        }
    }
    
    public static String hexadecimalABinario(String numeroHex) {
        try {
            numeroHex = numeroHex.toUpperCase().replace(",", ".");
            String[] partes = numeroHex.split("\\.");
    
            // Parte entera
            StringBuilder binEntero = new StringBuilder();
            for (char c : partes[0].toCharArray()) {
                int val = Integer.parseInt(String.valueOf(c), 16);
                binEntero.append(String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0'));
            }
    
            if (partes.length == 1) {
                return binEntero.toString();
            }
    
            // Parte fraccionaria
            StringBuilder binFraccion = new StringBuilder();
            for (char c : partes[1].toCharArray()) {
                int val = Integer.parseInt(String.valueOf(c), 16);
                binFraccion.append(String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0'));
            }
    
            return binEntero + "." + binFraccion;
    
        } catch (Exception e) {
            return "Número hexadecimal inválido";
        }
    }
    public static String binarioAHexadecimal(String numero) {
        try {
            String[] partes = numero.split("\\.");
            String parteEntera = partes[0];
            String parteFraccionaria = partes.length > 1 ? partes[1] : "";
    
            // Convertir parte entera
            int enteroDecimal = Integer.parseInt(parteEntera, 2);
            String enteroHex = Integer.toHexString(enteroDecimal).toUpperCase();
    
            // Convertir parte fraccionaria
            double fraccionDecimal = 0.0;
            for (int i = 0; i < parteFraccionaria.length(); i++) {
                if (parteFraccionaria.charAt(i) == '1') {
                    fraccionDecimal += 1.0 / Math.pow(2, i + 1);
                }
            }
    
            StringBuilder fraccionHex = new StringBuilder();
            double fraccion = fraccionDecimal;
            int precision = 6; // cantidad de dígitos hexadecimales deseados
    
            while (fraccion > 0 && fraccionHex.length() < precision) {
                fraccion *= 16;
                int digito = (int) fraccion;
                fraccion -= digito;
                fraccionHex.append(Integer.toHexString(digito).toUpperCase());
            }
    
            return fraccionHex.length() > 0 ? enteroHex + "." + fraccionHex : enteroHex;
    
        } catch (NumberFormatException e) {
            return "Número binario inválido";
        }
    }
    public static String iEEE_754ADecimal(String numero){ 
        // Paso 1 :convertir el numero hexadecimal a binario
        String binario = hexadecimalABinario(numero);

        // Paso 2 : Extraer el signom, el exponente y la mantisa
        String signo = binario.substring(0,1); //primer bit del signo
        String exponente = binario.substring(1,9); // Siguientes 8 bits del exponente
        String mantisa = "";
        if(!exponente.equals("00000000")){
            mantisa = "1" + binario.substring(9,32); // Siguientes 23 bits de la mantisa
        }else{
            mantisa = "0" + binario.substring(9,32); 
        }

        // Paso 3 : Verificar posible exepcion 
        if(exponente.equals("00000000") && mantisa.equals("00000000000000000000000")){
            return signo.equals("0") ? "+ 0" : "- 0";
        }
        if (exponente.equals("11111111") && mantisa.equals("00000000000000000000000")) {
            return signo.equals("0") ? "+ Infinito" : "-Infinito";
        }
        

        if (exponente.equals("11111111") && !mantisa.equals("00000000000000000000000")) {
            return "NaN";
        }

        // Paso 4 : Convertir en decimal
        int expDecimnal = Integer.parseInt(exponente,2) - 127;
        
        double mantisaDecimal = 0;
        for(int i = 0; i < mantisa.length(); i++){
            if(mantisa.charAt(i) == '1'){
                mantisaDecimal += Math.pow(2,expDecimnal -i);
            }
        }

        if(signo.equals("1") ){
            mantisaDecimal *= -1;
        }
        
        return String.valueOf(mantisaDecimal);



    }
    public static String IEEE_754_64BitsADecimal(String numero){
        // Paso 1 : Convertir el numero hexadecimal a binario
        String binario = hexadecimalABinario(numero);
        //return binario;

        // Paso 2: Extraigo los datos
        String signo = binario.substring(0,1);
        String exponente = binario.substring(1,12);
        String mantisa = "";
        if(!exponente.equals("00000000000")){
            mantisa = "1" + binario.substring(12,64);
        }else{
            mantisa = "0" + binario.substring(12,64);
        }

        //Paso 3: Verifico posibles excepciones
        if(exponente.equals("00000000000") && mantisa.equals("00000000000000000000000000000000000000000000000000000")){
            return signo.equals("0") ? "+ 0": "- 0";
        }
        if(exponente.equals("11111111111") && mantisa.equals("00000000000000000000000000000000000000000000000000000")){
            return signo.equals("0") ? " + Infinito " : "- Infinito";
        }

        String fraccionMantisa = mantisa.substring(1);

        if(exponente.equals("11111111111") && !fraccionMantisa.matches("0{52}")){
            return "NaN";
        }

        // Paso 4 : Convertir a decimal

        double mantisaDecimal = 0;
        for(int i = 0; i < mantisa.length(); i++){
            if(mantisa.charAt(i) == '1'){
                mantisaDecimal += Math.pow(2,  - i );
            }
        }

        int expDecimal = Integer.parseInt(exponente,2) - 1023;
        mantisaDecimal *= Math.pow(2, expDecimal);
        
        if(signo.equals("1")){
            mantisaDecimal *= -1;
        }

        return String.valueOf(mantisaDecimal);
    }
    public static String decimalAIEEE_754(String numero) {
    // 1. Signo
    int signo = (numero.charAt(0) == '-') ? 1 : 0;
    double valor = Math.abs(Double.parseDouble(numero));

    // 2. Obtener el exponente
    int exponente = 0;
    if (valor != 0) {
        exponente = (int) Math.floor(Math.log(valor) / Math.log(2));
    }

    // 3. Sesgo de 127
    int exponenteConSesgo = exponente + 127;

    // 4. Convertir exponente a binario (8 bits)
    String exponenteBinario = String.format("%8s", Integer.toBinaryString(exponenteConSesgo)).replace(' ', '0');

    // 5. Obtener mantisa (parte fraccional)
    double fraccion = valor / Math.pow(2, exponente) - 1; // quitar el 1 implícito
    StringBuilder mantisa = new StringBuilder();
    for (int i = 0; i < 23; i++) {
        fraccion *= 2;
        if (fraccion >= 1) {
            mantisa.append("1");
            fraccion -= 1;
        } else {
            mantisa.append("0");
        }
    }

    String resultado = binarioAHexadecimal(String.valueOf(signo) + exponenteBinario + mantisa.toString());
    return resultado;
    }
}