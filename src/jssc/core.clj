(ns jssc.core
  (:import [jssc
            SerialPortList
            SerialPort
            SerialPortException]))

(defrecord Port [path raw-port out-stream in-stream])

;; reference: http://java-simple-serial-connector.googlecode.com/svn/trunk/additional_content/javadoc/0.8/index.html

(defn port-ids
  "Returns a seq representing all port identifiers visible to the system"
  []
  (seq
   (SerialPortList/getPortNames)))

(defn list-ports
  "Print out the available ports. The names are printed exactly as they should be passed to open."
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

(defn write-string
  [port str]
  (.writeString port str))

(defn write-int
  [port number]
  (.writeInt port number))

(defn read-bytes
  [port count]
  (.readBytes port count))

(defn read-string
  ([port] (.readString port))
  ([port count] (.readString port count)))

(defn read-hex-string
  ([port count] (.readHexString port count))
  ([port count separator] (.readHexString port count separator)))

(defn read-bytes
  [port]
  (.readBytes port))


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
