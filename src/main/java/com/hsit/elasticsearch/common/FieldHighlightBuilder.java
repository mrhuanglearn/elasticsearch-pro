package com.hsit.elasticsearch.common;


import com.hsit.elasticsearch.abstracts.QueryOperationAbstract;
import com.hsit.elasticsearch.enums.AnalysisType;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FieldHighlightBuilder extends QueryOperationAbstract<FieldHighlightBuilder> {


    /**
     * ??????
     *
     * @return FieldHighlightBuilder
     */
    public static FieldHighlightBuilder builder() {
        return new FieldHighlightBuilder();
    }


    /**
     * ??????field
     *
     * @param fields list????
     * @return FieldHighlightBuilder
     */

    public FieldHighlightBuilder addField(List<String> fields) {
        if (ELKTools.isEmpty(fields)) {
            throw new IllegalArgumentException(" field must be not null");
        }
        fields.forEach(this::addField);
        return this;
    }


    /**
     * ??????field
     *
     * @param field ??field??
     * @return FieldHighlightBuilder
     */
    public FieldHighlightBuilder addField(String... field) {
        if (ELKTools.isEmpty(field)) {
            throw new IllegalArgumentException(" field must be not null");
        }
        this.fields.addAll(Arrays.asList(field));
        return this;
    }


    /**
     * ????????? analysisType
     *
     * @param analysisTypes ????(list ??)
     * @return FieldHighlightBuilder
     */
    public FieldHighlightBuilder addAnalysisType(List<AnalysisType> analysisTypes) {
        if (!ELKTools.isEmpty(analysisTypes)) {
            analysisTypes.forEach(this::addAnalysisType);
        }
        return this;
    }

    /**
     * ????????? analysisTypes
     *
     * @param analysisTypes ????????
     * @return FieldHighlightBuilder
     */
    public FieldHighlightBuilder addAnalysisType(AnalysisType... analysisTypes) {
        if (!ELKTools.isEmpty(analysisTypes)) {
            this.analysisTypes.addAll(Arrays.asList(analysisTypes));
        }
        return this;
    }


    /**
     * ??HighlightBuilder??
     *
     * @param fields ???????
     * @return HighlightBuilder
     */
    public HighlightBuilder getHighlightBuilder(String... fields) {
        return getHighlightBuilder(null, fields);
    }


    /**
     * ??HighlightBuilder??
     *
     * @param field ???????
     * @param style ????
     * @return HighlightBuilder
     */
    public HighlightBuilder getHighlightBuilder(String style, String field) {
        String[] strings = null;
        if (!Strings.isNullOrEmpty(field)) {
            strings = new String[]{field};
        }
        return getHighlightBuilder(style, strings);
    }

    /**
     * ??HighlightBuilder??
     *
     * @param style  ????
     * @param fields ???????
     * @return HighlightBuilder
     */
    public HighlightBuilder getHighlightBuilder(String style, String[] fields) {
        return getHighlightBuilder(style, null, fields);
    }

    /**
     * ??HighlightBuilder??
     *
     * @param highlightSign ????
     * @param style         ????
     * @param fields        ???????
     * @return HighlightBuilder
     */
    public HighlightBuilder getHighlightBuilder(String style, String highlightSign, String[] fields) {
        fields = Objects.requireNonNull(fields, "field must be not null");
        this.addField(fields);
        if (ELKTools.isEmpty(this.fields)) {
            throw new IllegalArgumentException(" field must be not null");
        }

        HighlightBuilder hiBuilder = new HighlightBuilder();
        if (Strings.isNullOrEmpty(highlightSign)) {
            highlightSign = "em";
        }

        if (Strings.isNullOrEmpty(style)) {
            style = "color:red";
        }
        hiBuilder.preTags("<" + highlightSign + " style=\"" + style + "\" >");
        hiBuilder.postTags("</" + highlightSign + ">");

        this.fields.forEach(field -> {
            if (this.analysisTypes.size() == 0) {
                hiBuilder.field(field);
            } else {
                this.analysisTypes.forEach(analysisType -> {
                    hiBuilder.field(field.concat(".").concat(analysisType.getValue()));
                });
            }
        });

        return hiBuilder;
    }


}
