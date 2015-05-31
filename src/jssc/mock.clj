(ns jssc.mock
  (:use [clojure.java.shell :only [sh]]))

(def ^:no-doc
  pid-list (atom []))

(defn- create-tmpfile
  "Create tmpfile for testing with socat. This function will return tmp file
  absolute path."
  []
  (let [tmp (java.io.File/createTempFile "tty" ".serial")]
    ;; mark the temporary file to be delete automatically when JVM exits.
    (.deleteOnExit tmp)
    ;; return absolute path
    (.getAbsolutePath tmp)))

;; {:pid "72928", :tty2 "/var/folders/ds/m99n8q4x4w9f84cj0r8ncc_40000gn/T/tty7055782053216333518.serial", :tty1 "/var/folders/ds/m99n8q4x4w9f84cj0r8ncc_40000gn/T/tty7669629127531817102.serial", :exit 0, :out "72928\n", :err ""}
(defn mock-serial
  "Create mocking serial by socat command. This function will create two virtual
  serial port for testing serial communication."
  []
  ;; Since we use socat to test this application, check if it exist first
  (when-not (zero? (:exit (sh "socat" "-h")))
    (throw (Exception. (str "Please install socat first."))))
  ;; Create virtual serial file
  (let [tty1 (create-tmpfile) tty2 (create-tmpfile)
        socat (format "socat PTY,raw,link=%s PTY,raw,link=%s" tty1 tty2)
        cmd (format "nohup %s < /dev/null &> /dev/null & echo $! " socat)
        exec (sh "bash" "-c" cmd)
        pid (clojure.string/trim (:out exec))]
    (if (zero? (:exit exec))
      ;; register pid info to let JVM know which process need to kill
      ;; before exist
      (swap! pid-list conj pid)
      ;; error case
      (throw (Exception. (str "socat execute failed, error: " pid))))
    ;; sleep a while for waith process done
    (Thread/sleep 2000)
    ;; return
    (merge exec {:tty1 tty1 :tty2 tty2 :pid pid})))

;; Before leave JVM, we need to close all socat process we create
(.addShutdownHook
 (Runtime/getRuntime)
 (Thread.
  (fn []
    ;; FIXME: why this will run twice ?
    (doseq [pid @pid-list]
      (sh "kill" "-SIGTERM" pid)
      ;; (println (format "kill socat with PID %s\n" pid))
      ))))