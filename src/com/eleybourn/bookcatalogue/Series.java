package com.eleybourn.bookcatalogue;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to hold book-related series data. Used in lists and import/export.
 * 
 * @author Grunthos
 */
public class Series implements Parcelable, Utils.ItemWithIdFixup {
	public long		id;
	public String 	name;
	public String	num;

	private java.util.regex.Pattern mPattern = java.util.regex.Pattern.compile("^(.*)\\s*\\((.*)\\)$");

	Series(String name) {
		java.util.regex.Matcher m = mPattern.matcher(name);
		if (m.find()) {
			this.name = m.group(1).trim();
			this.num = m.group(2).trim();
		} else {
			this.name = name.trim();
			this.num = "";
		}
		this.id = 0L;
	}

	Series(long id, String name) {
		this(id, name, "");
	}

	Series(String name, String num) {
		this(0L, name, num);
	}

	Series(long id, String name, String num) {
		this.id = id;
		this.name = name.trim();
		this.num = num.trim();
	}

	public String getDisplayName() {
		if (num != null && num.length() > 0)
			return name + " (" + num + ")";
		else
			return name;
	}

	public String getSortName() {
		return getDisplayName();
	}

	public String toString() {
		return getDisplayName();
	}

	/**
	 * Support for creation via Parcelable
	 */
    public static final Parcelable.Creator<Series> CREATOR
            = new Parcelable.Creator<Series>() {
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        public Series[] newArray(int size) {
            return new Series[size];
        }
    };
    
    private Series(Parcel in) {
    	name = in.readString();
    	num = in.readString();
    	id = in.readLong();
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(num);
		dest.writeLong(id);
	}

	@Override
	public long fixupId(CatalogueDBAdapter db) {
		this.id = db.lookupSeriesId(this);
		return this.id;
	}
}
