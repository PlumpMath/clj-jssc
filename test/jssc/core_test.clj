(ns jssc.core-test
  (:require [clojure.test :refer :all]
            [jssc.core :refer :all])
  (:use [jssc.mock]))

(deftest write-test
  (let [serial (mock-serial)
        tty1 (open (:tty1 serial))
        tty2 (open (:tty2 serial))]

    (testing "read/write string test"
      (let [pattern "This is testing read/write string"]
        ;; open another thread for listen tty2
        (future
          (Thread/sleep 10000)
          (is (= pattern (read-string tty2))))
        ;; write something
        (write-string tty1 pattern)))

    ;; remember to close opened serial port
    (close tty1)
    (close tty2)))
