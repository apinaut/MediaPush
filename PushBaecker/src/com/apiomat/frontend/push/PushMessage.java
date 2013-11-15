/*
 * Copyright (c) 2011-2013, Apinauten GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this 
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THIS FILE IS GENERATED AUTOMATICALLY. DON'T MODIFY IT.
 */
package com.apiomat.frontend.push;

import java.util.*;
import com.apiomat.frontend.*;
import com.apiomat.frontend.basics.*;
import com.apiomat.frontend.callbacks.*;
import com.apiomat.frontend.helper.*;

import rpc.json.me.*;

/**
 * Generated class for your PushMessage data model
 */
public class PushMessage extends AbstractClientDataModel {
	/**
	 * Default constructor. Needed for internal processing.
	 */
	public PushMessage() {
		super();
	}

	/**
	 * Returns the simple name of this class
	 */
	public String getSimpleName() {
		return "PushMessage";
	}

	/**
	 * Returns the name of the module where this class belongs to
	 */
	public String getModuleName() {
		return "Push";
	}

	/**
	 * Returns the system to connect to
	 */
	public String getSystem() {

		return User.system;
	}

	/**
	 * Returns a list of objects of this class filtered by the given query from
	 * server
	 * 
	 * @query a query filtering the results in SQL style (@see <a
	 *        href="http://doc.apiomat.com">documentation</a>)
	 */
	public static final List<PushMessage> getPushMessages(String query)
			throws ApiomatRequestException {
		PushMessage o = new PushMessage();
		return Datastore.getInstance().loadFromServer(PushMessage.class,
				o.getModuleName(), o.getSimpleName(), query);
	}

	/**
	 * Get a list of objects of this class filtered by the given query from
	 * server This method works in the background and call the callback function
	 * when finished
	 * 
	 * @param query
	 *            a query filtering the results in SQL style (@see <a
	 *            href="http://doc.apiomat.com">documentation</a>)
	 * @param listAOMCallback
	 *            The callback method which will called when request is finished
	 */
	public static void getPushMessagesAsync(final String query,
			final AOMCallback<List<PushMessage>> listAOMCallback) {
		getPushMessagesAsync(query, false, listAOMCallback);
	}

	/**
	 * Returns a list of objects of this class filtered by the given query from
	 * server
	 * 
	 * @query a query filtering the results in SQL style (@see <a
	 *        href="http://doc.apiomat.com">documentation</a>)
	 * @param withReferencedHrefs
	 *            set to true to get also all HREFs of referenced models
	 */
	public static final List<PushMessage> getPushMessages(String query,
			boolean withReferencedHrefs) throws Exception {
		PushMessage o = new PushMessage();
		return Datastore.getInstance().loadFromServer(PushMessage.class,
				o.getModuleName(), o.getSimpleName(), withReferencedHrefs,
				query);
	}

	/**
	 * Get a list of objects of this class filtered by the given query from
	 * server This method works in the background and call the callback function
	 * when finished
	 * 
	 * @param query
	 *            a query filtering the results in SQL style (@see <a
	 *            href="http://doc.apiomat.com">documentation</a>)
	 * @param withReferencedHrefs
	 *            set true to get also all HREFs of referenced models
	 * @param listAOMCallback
	 *            The callback method which will called when request is finished
	 */
	public static void getPushMessagesAsync(final String query,
			final boolean withReferencedHrefs,
			final AOMCallback<List<PushMessage>> listAOMCallback) {
		PushMessage o = new PushMessage();
		Datastore.getInstance().loadFromServerAsync(PushMessage.class,
				o.getModuleName(), o.getSimpleName(), withReferencedHrefs,
				query, listAOMCallback);
	}

	public List getReceiverUserNames() {
		JSONArray array = (JSONArray) this.data.opt("receiverUserNames");
		return fromJSONArray(array);
	}

	public void setReceiverUserNames(List arg) {
		Vector receiverUserNames = toVector(arg);
		this.data.put("receiverUserNames", receiverUserNames);
	}

	public Long getScheduleTimestamp() {
		return this.data.optLong("scheduleTimestamp");
	}

	public void setScheduleTimestamp(Long arg) {
		Long scheduleTimestamp = arg;
		this.data.put("scheduleTimestamp", scheduleTimestamp);
	}

	public Integer getSuccessCount() {
		return this.data.optInt("successCount");
	}

	public void setSuccessCount(Integer arg) {
		Integer successCount = arg;
		this.data.put("successCount", successCount);
	}

	public void setImageURL(String arg) {
		String imageURL = arg;
		this.data.put("imageURL", imageURL);
	}

	public String getPayload() {
		return this.data.optString("payload");
	}

	public void setPayload(String arg) {
		String payload = arg;
		this.data.put("payload", payload);
	}

	public Map getCustomData() {
		return this.data.optJSONObject("customData").getMyHashMap();
	}

	public void setCustomData(Map map) {
		if (!this.data.has("customData")) {
			this.data.put("customData", new Hashtable());
		} else {
			this.data.optJSONObject("customData").getMyHashMap().clear();
		}
		this.data.optJSONObject("customData").getMyHashMap().putAll(map);
	}

	/**
	 * Returns the URL of the resource.
	 * 
	 * @return the URL of the resource
	 */
	public String getFileURL() {
		if (this.data.isNull("fileURL")) {
			return null;
		}
		return this.data.optString("fileURL") + ".img?apiKey=" + User.apiKey
				+ "&system=" + this.getSystem();
	}

	public String postFile(byte[] data) throws Exception {
		String href = null;
		if (Datastore.getInstance().sendOffline("POST")) {
			final String sendHREF = Datastore.getInstance()
					.createStaticDataHref(true);
			href = Datastore.getInstance().getOfflineHandler()
					.addTask("POST", sendHREF, data);
		} else {
			href = Datastore.getInstance().postStaticDataOnServer(data, false);
		}

		if (href != null && href.length() > 0) {
			this.data.put("fileURL", href);
			this.save();
		}
		return href;
	}

	public void postFileAsync(final byte[] data,
			final AOMEmptyCallback _callback) {
		AOMCallback<String> cb = new AOMCallback<String>() {
			@Override
			public void isDone(String href, ApiomatRequestException ex) {
				if (ex == null && href != null && href.length() > 0) {
					PushMessage.this.data.put("fileURL", href);
					/* save new image reference in model */
					PushMessage.this.saveAsync(new AOMEmptyCallback() {
						@Override
						public void isDone(ApiomatRequestException exception) {
							if (_callback != null) {
								_callback.isDone(exception);
							} else {
								System.err.println("Exception was thrown: "
										+ exception.getMessage());
							}
						}
					});
				} else {
					if (_callback != null && ex != null) {
						_callback.isDone(ex);
					} else if (_callback != null && ex == null) {
						_callback.isDone(new ApiomatRequestException(
								Status.HREF_NOT_FOUND));
					} else {
						System.err.println("Exception was thrown: "
								+ (ex != null ? ex.getMessage()
										: Status.HREF_NOT_FOUND.toString()));
					}
				}
			}
		};

		if (Datastore.getInstance().sendOffline("POST")) {
			final String sendHREF = Datastore.getInstance()
					.createStaticDataHref(true);
			String refHref = Datastore.getInstance().getOfflineHandler()
					.addTask("POST", sendHREF, data);
			cb.isDone(refHref, null);
		} else {
			Datastore.getInstance().postStaticDataOnServerAsync(data, true, cb);
		}
	}

	public void deleteFile() throws Exception {
		final String imageURL = this.data.optString("fileURL");
		this.data.remove("fileURL");
		if (Datastore.getInstance().sendOffline("DELETE")) {
			Datastore.getInstance().getOfflineHandler()
					.addTask("DELETE", imageURL);
			this.save();
		} else {
			Datastore.getInstance().deleteOnServer(imageURL);
			this.save();
		}
	}

	public void deleteFileAsync(final AOMEmptyCallback _callback) {
		AOMEmptyCallback cb = new AOMEmptyCallback() {
			@Override
			public void isDone(ApiomatRequestException ex) {
				if (ex == null) {
					PushMessage.this.data.remove("fileURL");
					/* save deleted image reference in model */
					PushMessage.this.saveAsync(new AOMEmptyCallback() {
						@Override
						public void isDone(ApiomatRequestException exception) {
							if (_callback != null) {
								_callback.isDone(exception);
							} else {
								System.err.println("Exception was thrown: "
										+ exception.getMessage());
							}
						}
					});
				}
				_callback.isDone(ex);
			}
		};
		final String url = this.data.optString("fileURL");
		if (Datastore.getInstance().sendOffline("DELETE")) {
			Datastore.getInstance().getOfflineHandler().addTask("DELETE", url);
			cb.isDone(null);
		} else {
			Datastore.getInstance().deleteOnServerAsync(url, cb);
		}
	}

	public String getQuery() {
		return this.data.optString("query");
	}

	public void setQuery(String arg) {
		String query = arg;
		this.data.put("query", query);
	}

	public Integer getTimeToLive() {
		return this.data.optInt("timeToLive");
	}

	public void setTimeToLive(Integer arg) {
		Integer timeToLive = arg;
		this.data.put("timeToLive", timeToLive);
	}

	public Boolean getPushWasSent() {
		return this.data.optBoolean("pushWasSent");
	}

	public void setPushWasSent(Boolean arg) {
		Boolean pushWasSent = arg;
		this.data.put("pushWasSent", pushWasSent);
	}

	public String getReceiverUserName() {
		return this.data.optString("receiverUserName");
	}

	public void setReceiverUserName(String arg) {
		String receiverUserName = arg;
		this.data.put("receiverUserName", receiverUserName);
	}

	public Integer getBadge() {
		return this.data.optInt("badge");
	}

	public void setBadge(Integer arg) {
		Integer badge = arg;
		this.data.put("badge", badge);
	}

	public Integer getFailureCount() {
		return this.data.optInt("failureCount");
	}

	public void setFailureCount(Integer arg) {
		Integer failureCount = arg;
		this.data.put("failureCount", failureCount);
	}

	public void setFileURL(String arg) {
		String fileURL = arg;
		this.data.put("fileURL", fileURL);
	}

	/**
	 * Returns the URL of the resource.
	 * 
	 * @return the URL of the resource
	 */
	public String getImageURL() {
		if (this.data.isNull("imageURL")) {
			return null;
		}
		return this.data.optString("imageURL") + ".img?apiKey=" + User.apiKey
				+ "&system=" + this.getSystem();
	}

	public String postImage(byte[] data) throws Exception {
		String href = null;
		if (Datastore.getInstance().sendOffline("POST")) {
			final String sendHREF = Datastore.getInstance()
					.createStaticDataHref(true);
			href = Datastore.getInstance().getOfflineHandler()
					.addTask("POST", sendHREF, data);
		} else {
			href = Datastore.getInstance().postStaticDataOnServer(data, true);
		}

		if (href != null && href.length() > 0) {
			this.data.put("imageURL", href);
			this.save();
		}
		return href;
	}

	public void postImageAsync(final byte[] data,
			final AOMEmptyCallback _callback) {
		AOMCallback<String> cb = new AOMCallback<String>() {
			@Override
			public void isDone(String href, ApiomatRequestException ex) {
				if (ex == null && href != null && href.length() > 0) {
					PushMessage.this.data.put("imageURL", href);
					/* save new image reference in model */
					PushMessage.this.saveAsync(new AOMEmptyCallback() {
						@Override
						public void isDone(ApiomatRequestException exception) {
							if (_callback != null) {
								_callback.isDone(exception);
							} else {
								System.err.println("Exception was thrown: "
										+ exception.getMessage());
							}
						}
					});
				} else {
					if (_callback != null && ex != null) {
						_callback.isDone(ex);
					} else if (_callback != null && ex == null) {
						_callback.isDone(new ApiomatRequestException(
								Status.HREF_NOT_FOUND));
					} else {
						System.err.println("Exception was thrown: "
								+ (ex != null ? ex.getMessage()
										: Status.HREF_NOT_FOUND.toString()));
					}
				}
			}
		};

		if (Datastore.getInstance().sendOffline("POST")) {
			final String sendHREF = Datastore.getInstance()
					.createStaticDataHref(true);
			String refHref = Datastore.getInstance().getOfflineHandler()
					.addTask("POST", sendHREF, data);
			cb.isDone(refHref, null);
		} else {
			Datastore.getInstance().postStaticDataOnServerAsync(data, true, cb);
		}
	}

	public void deleteImage() throws Exception {
		final String imageURL = this.data.optString("imageURL");
		this.data.remove("imageURL");
		if (Datastore.getInstance().sendOffline("DELETE")) {
			Datastore.getInstance().getOfflineHandler()
					.addTask("DELETE", imageURL);
			this.save();
		} else {
			Datastore.getInstance().deleteOnServer(imageURL);
			this.save();
		}
	}

	public void deleteImageAsync(final AOMEmptyCallback _callback) {
		AOMEmptyCallback cb = new AOMEmptyCallback() {
			@Override
			public void isDone(ApiomatRequestException ex) {
				if (ex == null) {
					PushMessage.this.data.remove("imageURL");
					/* save deleted image reference in model */
					PushMessage.this.saveAsync(new AOMEmptyCallback() {
						@Override
						public void isDone(ApiomatRequestException exception) {
							if (_callback != null) {
								_callback.isDone(exception);
							} else {
								System.err.println("Exception was thrown: "
										+ exception.getMessage());
							}
						}
					});
				}
				_callback.isDone(ex);
			}
		};
		final String url = this.data.optString("imageURL");
		if (Datastore.getInstance().sendOffline("DELETE")) {
			Datastore.getInstance().getOfflineHandler().addTask("DELETE", url);
			cb.isDone(null);
		} else {
			Datastore.getInstance().deleteOnServerAsync(url, cb);
		}
	}

	/**
	 * Returns an URL of the image. <br/>
	 * You can provide several parameters to manipulate the image:
	 * 
	 * @param width
	 *            the width of the image, 0 to use the original size. If only
	 *            width or height are provided, the other value is computed.
	 * @param height
	 *            the height of the image, 0 to use the original size. If only
	 *            width or height are provided, the other value is computed.
	 * @param backgroundColorAsHex
	 *            the background color of the image, null or empty uses the
	 *            original background color. Caution: Don't send the '#' symbol!
	 *            Example: <i>ff0000</i>
	 * @param alpha
	 *            the alpha value of the image, null to take the original value.
	 * @param format
	 *            the file format of the image to return, e.g. <i>jpg</i> or
	 *            <i>png</i>
	 * @return the URL of the image
	 */
	public String getImageURL(int width, int height,
			String backgroundColorAsHex, Double alpha, String format) {
		String parameters = ".img?apiKey=" + User.apiKey + "&system="
				+ this.getSystem();
		parameters += "&width=" + width + "&height=" + height;
		if (backgroundColorAsHex != null) {
			parameters += "&bgcolor=" + backgroundColorAsHex;
		}
		if (alpha != null)
			parameters += "&alpha=" + alpha;
		if (format != null)
			parameters += "&format=" + format;
		return this.data.optString("imageURL") + parameters;
	}

	public List getFailureReasons() {
		JSONArray array = (JSONArray) this.data.opt("failureReasons");
		return fromJSONArray(array);
	}

	public void setFailureReasons(List arg) {
		Vector failureReasons = toVector(arg);
		this.data.put("failureReasons", failureReasons);
	}

	public void send() throws ApiomatRequestException {
		Datastore.getInstance().updateOnServer(this.getHref() + "/method/send",
				"[]");
	}

	public void sendAsync(AOMEmptyCallback callback) {
		Datastore.getInstance().updateOnServerAsync(
				this.getHref() + "/method/send", "[]", callback);
	}
}
