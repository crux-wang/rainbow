package ren.crux.rainbow.core.report.html;

import ren.crux.rainbow.core.model.*;
import ren.crux.rainbow.core.report.Reporter;

import java.util.List;

public interface HtmlReporter extends Reporter<String> {

    String reportRequestGroups(List<RequestGroup> requestGroups);

    String reportRequestGroup(RequestGroup requestGroup);

    String reportRequests(List<Request> requests);

    String report(Request request);

    String reportRequestParamGroups(List<RequestParam> requestParams);

    String reportRequestParams(List<RequestParam> requestParams);

    String reportRequestParam(RequestParam requestParam);

    String reportRequestMethods(RequestMethod[] method);

    String reportPaths(String[] paths);

    String reportEntries(List<Entry> entries);

    String reportEntry(Entry entry);

    String reportEntryFields(List<EntryField> entryFields);

    String reportEntryField(EntryField entryField);

}
