package org.kie.server.custom.ext.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class CustomPrometheusMetricUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomPrometheusMetricUtil.class);

	public static CollectorRegistry prometheusRegistry = CollectorRegistry.defaultRegistry;
	
	public static boolean doesMetricRegistered(final String metricName) {
		Enumeration<Collector.MetricFamilySamples> emfs = prometheusRegistry.metricFamilySamples();
		List<String> names = registeredNames(Collections.list(emfs));
		LOGGER.debug("Existing name in Registry" + names);
		if(names.contains(metricName)) {
			return  true;
		}
		else {
			return false;
		}
	}
	
	public static Counter registerCounterMetric(final String metricName, String metricHelpDesc, String... labelNames) {
		return Counter.build()
        .name(metricName)
        .help(metricHelpDesc)
        .labelNames(labelNames)
        .register();
	}
	
	public static Gauge registerGaugeMetric1(final String metricName, String metricHelpDesc, String... labelNames) {
		return Gauge.build()
        .name(metricName)
        .help(metricHelpDesc)
        .labelNames(labelNames)
        .register();
	}
	
	public static Gauge registerGaugeMetric(final String metricName, String metricHelpDesc, String... labelNames) {
		return Gauge.build()
        .name(metricName)
        .help(metricHelpDesc)
        .labelNames(labelNames)
        .register();
	}
	
	private static List<String> registeredNames(final List<Collector.MetricFamilySamples> mfs) {
	    List<String> names = new ArrayList<String>();
	    for (Collector.MetricFamilySamples family : mfs) {
	      switch (family.type) {
	        case SUMMARY:
	          names.add(family.name + "_count");
	          names.add(family.name + "_sum");
	          names.add(family.name);
	          break;
	        case HISTOGRAM:
	          names.add(family.name + "_count");
	          names.add(family.name + "_sum");
	          names.add(family.name + "_bucket");
	          names.add(family.name);
	          break;
	        default:
	          names.add(family.name);
	      }
	    }
	    return names;
	  }

}
