package org.godker.utils

enum class AFIPServices(val value: String, val description: String) {
    WSFEv1  ("wsfe",    "Factura Electrónica v1"),
    WSSEG   ("wsseg",   "Seguros de Caución"),
    WSMTXCA ("wsmtxca", "Factura Electrónica con detalle de Items"),
    WSBFEv1 ("wsbfev1", "Bonos Fiscales Electrónicos v1"),
    WSFEXv1 ("wsfexv1", "Factura Electrónica de Exportación v1"),
    WSCT    ("wsct",    "Comprobantes T para turismo")
}