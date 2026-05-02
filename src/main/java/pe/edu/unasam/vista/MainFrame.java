package pe.edu.unasam.vista;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

public class MainFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    // --- Variables de Lógica ---
    private List<String> universoV = new ArrayList<>();
    private String[] vocabularioGuardado;
    private int longitudMaximaGuardada;
    private int cantidadTotalLenguajes = 0;
    private int lenguajeActual = 1;
    private Predicate<String> condicionActual = palabra -> true;
    private int propiedadesAgregadas = 0;

    public MainFrame() {
        initComponents();

        this.setTitle("Generador de Lenguajes Formales");
        this.setLocationRelativeTo(null);

        cbxTipo.removeAllItems();
        cbxTipo.addItem("Longitud exacta");
        cbxTipo.addItem("Inicia con");
        cbxTipo.addItem("Termina con");
        cbxTipo.addItem("Contiene");

        // Bloquear botones de propiedades hasta que el universo sea válido
        btnAnadir.setEnabled(false);
        btnGenerar.setEnabled(false);

    }

    private void configurarComponentesManual() {
        // Configurar el ComboBox con las propiedades
        cbxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Longitud exacta", "Inicia con", "Termina con", "Contiene"
        }));

        // Bloquear panel de propiedades hasta validar universo
        habilitarPanelPropiedades(false);

        // --- Asignación de Eventos ---
        btnValidar.addActionListener(evt -> accionValidarUniverso());
        btnAnadir.addActionListener(evt -> accionAnadirPropiedad());
        btnGenerar.addActionListener(evt -> accionGenerarLenguaje());
    }

    // --- LÓGICA DE EVENTOS ---
    private void accionValidarUniverso() {
        String inputVocab = txtVocabulario.getText().trim();
        if (inputVocab.isEmpty()) {
            mostrarError("El vocabulario no puede estar vacío.");
            return;
        }

        // Limpiar y validar símbolos duplicados
        String[] rawVocab = inputVocab.split(",");
        Set<String> vocabSet = new HashSet<>();
        for (String s : rawVocab) {
            String limpio = s.trim();
            if (!limpio.isEmpty()) {
                vocabSet.add(limpio);
            }
        }

        if (vocabSet.isEmpty()) {
            mostrarError("Ingrese al menos un símbolo válido.");
            return;
        }

        vocabularioGuardado = vocabSet.toArray(new String[0]);

        try {
            longitudMaximaGuardada = Integer.parseInt(txtLongitud.getText().trim());
            cantidadTotalLenguajes = Integer.parseInt(txtCantidad.getText().trim());

            if (longitudMaximaGuardada <= 0 || cantidadTotalLenguajes <= 0) {
                mostrarError("Los valores numéricos deben ser mayores a 0.");
                return;
            }

            // Generar V*
            universoV.clear();
            generarUniverso(vocabularioGuardado, "", longitudMaximaGuardada);

            txtaResultados.setText("SISTEMA INICIALIZADO\n");
            txtaResultados.append("Vocabulario V = {" + String.join(", ", vocabularioGuardado) + "}\n");
            txtaResultados.append("Universo V* generado: " + universoV.size() + " palabras.\n");
            txtaResultados.append("--------------------------------------------------\n");

            // Bloquear configuración inicial
            btnValidar.setEnabled(false);
            txtVocabulario.setEnabled(false);
            txtLongitud.setEnabled(false);
            txtCantidad.setEnabled(false);

            lenguajeActual = 1;
            reiniciarCondicionesLenguaje();
            habilitarPanelPropiedades(true);

        } catch (NumberFormatException ex) {
            mostrarError("La longitud y cantidad deben ser números enteros.");
        }
    }

    private void accionAnadirPropiedad() {
        String tipo = (String) cbxTipo.getSelectedItem();
        String valor = txtValor.getText().trim();

        if (valor.isEmpty()) {
            mostrarError("Ingrese un valor para la propiedad.");
            return;
        }

        try {
            if (tipo.equals("Longitud exacta")) {
                int len = Integer.parseInt(valor);
                if (len < 0) {
                    mostrarError("No existen longitudes negativas.");
                    return;
                }
                condicionActual = condicionActual.and(p -> p.length() == len);
            } else {
                // Validar que los caracteres de la propiedad existan en el vocabulario
                if (!esCadenaValida(valor)) {
                    mostrarError("El valor contiene símbolos ajenos al vocabulario.");
                    return;
                }
                switch (tipo) {
                    case "Inicia con":
                        condicionActual = condicionActual.and(p -> p.startsWith(valor));
                        break;
                    case "Termina con":
                        condicionActual = condicionActual.and(p -> p.endsWith(valor));
                        break;
                    case "Contiene":
                        condicionActual = condicionActual.and(p -> p.contains(valor));
                        break;
                }
            }
            propiedadesAgregadas++;
            txtValor.setText("");
            txtaResultados.append("L" + lenguajeActual + " -> Agregada: " + tipo + " (" + valor + ")\n");
        } catch (NumberFormatException ex) {
            mostrarError("Para longitud, ingrese un número.");
        }
    }

    private void accionGenerarLenguaje() {
        List<String> resultado = universoV.stream()
                .filter(condicionActual)
                .collect(Collectors.toList());

        txtaResultados.append(">>> RESULTADO L" + lenguajeActual + " = {"
                + (resultado.isEmpty() ? "Ø" : String.join(", ", resultado)) + "}\n");
        txtaResultados.append("--------------------------------------------------\n");

        lenguajeActual++;

        if (lenguajeActual > cantidadTotalLenguajes) {
            habilitarPanelPropiedades(false);
            JOptionPane.showMessageDialog(this, "Se han generado todos los lenguajes.");
        } else {
            reiniciarCondicionesLenguaje();
        }
    }

    // --- MÉTODOS DE APOYO ---
    private void generarUniverso(String[] vocab, String actual, int max) {
        if (!actual.isEmpty()) {
            universoV.add(actual);
        }
        if (actual.length() < max) {
            for (String s : vocab) {
                generarUniverso(vocab, actual + s, max);
            }
        }
    }

    private boolean esCadenaValida(String cadena) {
        String v = String.join("", vocabularioGuardado);
        for (char c : cadena.toCharArray()) {
            if (v.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    private void habilitarPanelPropiedades(boolean b) {
        cbxTipo.setEnabled(b);
        txtValor.setEnabled(b);
        btnAnadir.setEnabled(b);
        btnGenerar.setEnabled(b);
    }

    private void reiniciarCondicionesLenguaje() {
        condicionActual = p -> true;
        propiedadesAgregadas = 0;
        ((TitledBorder) jPanel2.getBorder()).setTitle("Definir propiedades para L" + lenguajeActual);
        jPanel2.repaint();
    }

    private void mostrarError(String m) {
        JOptionPane.showMessageDialog(this, m, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtVocabulario = new javax.swing.JTextField();
        txtLongitud = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        btnValidar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbxTipo = new javax.swing.JComboBox<>();
        txtValor = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        btnGenerar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaResultados = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuracion de Universo"));
        jPanel1.setToolTipText("");

        jLabel1.setText("Vocabulario (separado por una coma)");

        jLabel2.setText("Longitud maxima de palabra");

        jLabel3.setText("Cantidad de lenguaje a generar");

        btnValidar.setText("Validar e Inicializar Universo");
        btnValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVocabulario, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnValidar, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtVocabulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(btnValidar)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Definir propiedades"));

        jLabel4.setText("Tipo de propiedad:");

        jLabel5.setText("Valor de propiedad");

        cbxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAnadir.setText("Añadir Propiedad al Lenguaje");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });

        btnGenerar.setText("Finalizar y Generar Lenguaje");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAnadir, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(btnGenerar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnadir))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerar))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados y Validadaciones"));

        txtaResultados.setColumns(20);
        txtaResultados.setRows(5);
        jScrollPane1.setViewportView(txtaResultados);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarActionPerformed
        // TODO add your handling code here:
        accionValidarUniverso();
    }//GEN-LAST:event_btnValidarActionPerformed

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        // TODO add your handling code here:
        accionAnadirPropiedad();
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        // TODO add your handling code here:
        accionGenerarLenguaje();
    }//GEN-LAST:event_btnGenerarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnValidar;
    private javax.swing.JComboBox<String> cbxTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtLongitud;
    private javax.swing.JTextField txtValor;
    private javax.swing.JTextField txtVocabulario;
    private javax.swing.JTextArea txtaResultados;
    // End of variables declaration//GEN-END:variables
}
