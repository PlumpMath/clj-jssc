(ns jssc.core
  (:import [jssc
            SerialPortList
            SerialPort
            SerialPortException]))

(defn port-ids
  "Returns a seq representing all port identifiers visible to the system"
  []
  (seq
   (SerialPortList/getPortNames)))

(defn list-ports
  "Print out the available ports with an index number for future reference
  with (port-at <i>)."
  []
  (apply println (port-ids)))


(defn close
  "Close an open port."
  [port]
  (.closePort port))

(defn open
  ([path] (open path 115200))
  ([path baud-rate]
   (try
     (let [serial (SerialPort. path)]
       (.openPort serial)
       (.setParams serial
                   SerialPort/BAUDRATE_9600
                   SerialPort/DATABITS_8
                   SerialPort/STOPBITS_1
                   SerialPort/PARITY_NONE)
       serial)
     (catch SerialPortException e
       (throw (Exception. (str "Sorry, couldn't connect to the port with path " path )))))))

;; Simple testing code
(comment
  ;; we can use socat to create virtual serial port
  ;; open serial and type following command
  ;;
  ;;  socat -d -d pty pty

  ;; open serial, you can use tty0tty to test it
  (def conn (open "/dev/pts/6"))
  ;; write some info
  (.writeBytes conn (.getBytes "asdadads"))
  ;; close connection
  (close conn))
