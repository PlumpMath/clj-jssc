(ns jssc.core-test
  (:require [clojure.test :refer :all]
            [jssc.core :refer :all])
  (:use [jssc.test-helper]))

;;(. (Runtime/getRuntime) exec (into-array ["socat" "-d" "-d" "pty" "pty"]))

;; (deftest a-test
;;   (let [s (mock-serial)]
;;     (println (str s))
;;     (testing "FIXME, I fail."
;;       (is (= 0 1)))
;;     ))

(deftest write-test
  (let [serial (mock-serial)
        tty1 (:tty1 serial)
        tty2 (:tty2 serial)]

    (testing "simple write"
      (let [conn (open tty1)]
        (.writeBytes conn (.getBytes "asdadads"))
        (close conn)
        ))
    ))
