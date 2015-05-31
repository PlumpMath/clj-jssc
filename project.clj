(defproject clj-jssc "0.1.0-SNAPSHOT"
  :description "Simple serial port comms library. Wraps jssc."
  :url "http://github.com/coldnew/clj-jssc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.scream3r/jssc "2.8.0"]]

  :plugins [[codox "0.8.12"]]
  :codox {:src-dir-uri "http://github.com/coldnew/clj-jssc/blob/master/"
          :src-linenum-anchor-prefix "L"})
